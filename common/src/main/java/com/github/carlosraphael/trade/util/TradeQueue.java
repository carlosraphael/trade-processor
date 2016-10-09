package com.github.carlosraphael.trade.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

import org.agrona.concurrent.OneToOneConcurrentArrayQueue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.carlosraphael.trade.model.Trade;

import lombok.extern.slf4j.Slf4j;

/**
 * Component wraps a single consumer/producer trade queue. it exposes async method to store trade backed by a 
 * single thread executor(producer) and sync method to drain the queue and the fallback queue if any.
 * 
 * A consumer must be implemented and must handle the queue draining and destruction.
 * 
 * @author carlos
 */
@Slf4j
@Component
public class TradeQueue implements InitializingBean {

	private OneToOneConcurrentArrayQueue<Trade> queue;
	private OneToOneConcurrentArrayQueue<Trade> fallback;
	private LongAdder fallbackCount;
	private ForkJoinPool queueProducer;
	
	@Value("${trade.queue.capacity:50000}")
	private int capacity = 50000;
	
	public TradeQueue() {}
	public TradeQueue(int capacity) {this.capacity = capacity;}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		queueProducer = (ForkJoinPool) Executors.newWorkStealingPool(1);
		queue = new OneToOneConcurrentArrayQueue<>(capacity);
		fallback = new OneToOneConcurrentArrayQueue<>(capacity/4 < 1000 ? 1000 : capacity/4);
		fallbackCount = new LongAdder();
	}
	
	/**
	 * method must be called by the consumer implementation {@link TradeServiceImpl}
	 */
	public void destroy() throws Exception {
		queueProducer.shutdown();
		queueProducer.awaitTermination(1, MINUTES);
	}
	
	/**
	 * Async method which relies on {@link Runnable} in the {@link TradeQueue#queueProducer}.
	 * 
	 * In case the queue gets full the runnable will be locked by 10ms and then the retry will be performed once more. 
	 * If even after the second attempt the queue is still full, it's gonna be used the {@link TradeQueue#fallback}.
	 *  
	 * @param trade
	 */
	public void offer(final Trade trade) { //TODO REVIEW
		queueProducer.execute(() -> {
			int attempts = 1;
			while (!queue.offer(trade)) {
				if (log.isDebugEnabled())
					log.debug("TradeQueue is full");
				
				if (attempts > 2) {
					if (!fallback.offer(trade))
						log.error(String.format("[FATAL] Fallback error for trade: %s", trade.toString()));
					else
						fallbackCount.increment();
					return;
				}
				LockSupport.parkNanos(MILLISECONDS.toNanos(10)); // 10ms of penalty as queue is full
				attempts++;
			}
		});
	}
	
	/**
	 * Sync method to drain the queue as it's being used a single producer/consumer to get the lowest latency possible. 
	 * 
	 * It's being reserved some space to always drain the fallback if some trade exists. However, if fallback capacity
	 * is less than 1/4, fallback will be entirely drained. 
	 * 
	 * @param limit - amount of trade to be drained
	 * @return {@link List} - queued elements 
	 */
	public synchronized List<Trade> drain(final int limit) {
		List<Trade> result = null;
		
		if (limit == 0)
			throw new IllegalStateException();
		
		final int fallbackCurrentSize = fallback.size();
		final int fallbackCapacity = fallback.capacity();
		
		if (fallbackCapacity-fallbackCurrentSize < fallbackCapacity/4) {
			result = new ArrayList<>(limit + fallbackCurrentSize);
			queue.drainTo(result, limit);
			fallback.drainTo(result, fallbackCurrentSize);
		} else {
			final int reserved = limit >= 20 ? limit-10 : limit-1;
			
			result = new ArrayList<>(limit);
			queue.drainTo(result, limit-reserved);
			fallback.drainTo(result, reserved);
			
			if (result.size() < limit)
				queue.drainTo(result, limit - result.size());
		}
		
		return result;
	}
	
	/**
	 * @return {@code int} - current queue size plus fallback size
	 */
	public int size() {
		return queue.size() + fallback.size();
	}

	/**
	 * @return {@code long} - estimated amount of threads waiting for being executed
	 */
	public long countWaitingProducers() {
		return queueProducer.getQueuedTaskCount();
	}
	
	/**
	 * @return {@code long} - fallback grand total errors
	 */
	public long countFallback() {
		return fallbackCount.sum();
	}
}
