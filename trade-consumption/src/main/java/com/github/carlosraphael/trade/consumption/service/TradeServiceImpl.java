package com.github.carlosraphael.trade.consumption.service;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.YieldingIdleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.repository.TradeRepository;
import com.github.carlosraphael.trade.service.client.TradeProcessorClient;
import com.github.carlosraphael.trade.util.TradeQueue;

import lombok.extern.slf4j.Slf4j;

/**
 * Async implementation backed by a single producer/consumer queue and responsible for consuming trade by 
 * saving them in the {@link TradeRepository}
 * 
 * {@link TradeServiceImpl#consumerBatchSize} has initial value of 2k and can be easily changed via JMX
 * 
 * @author carlos
 */
@Slf4j
@Service
@ManagedResource(description = "Service responsible for managing received trade")
public class TradeServiceImpl implements TradeService {

	private static final String ARGUMENT_REQUIRED = "Argument cannot be null";
	
	private final TradeProcessorClient tradeProcessorClient;
	private final TradeQueue tradeQueue;
	private final TradeRepository tradeRepository;
	private final ExecutorService tradeQueueConsumer;
	private final LongAdder totalReceived = new LongAdder();
	private final AtomicBoolean shutdownRequested = new AtomicBoolean(false);
	private final AtomicInteger consumerBatchSize = new AtomicInteger(2000); // FIXME make it external configurable
	
	@Autowired
	public TradeServiceImpl(final TradeRepository tradeRepository, final TradeQueue tradeQueue,
			final TradeProcessorClient tradeProcessorClient) {
		this.tradeRepository = tradeRepository;
		this.tradeQueue = tradeQueue;
		this.tradeProcessorClient = tradeProcessorClient;
		this.tradeQueueConsumer = Executors.newSingleThreadExecutor();
	}
	
	@Override
	public void afterPropertiesSet() {
		// init queue consumer
		tradeQueueConsumer.execute(() -> {
			final IdleStrategy idleStrategy = new YieldingIdleStrategy(); // pergahps make it configurable
			do {
				final List<Trade> listReceivedtrade = tradeQueue.drain(getConsumerBatchSize());
				if (!listReceivedtrade.isEmpty()) {
					try {
						tradeProcessorClient.batchProcessForRealtime(listReceivedtrade);
					} catch (RuntimeException e) {
						// ignore exception. should be using a proper feign fallback <<
					} finally {
						tradeRepository.save(listReceivedtrade);
					}
				}
				idleStrategy.idle(listReceivedtrade.size());
			} while (!isShutdownRequested());
		});
	}
	
	/**
	 * Gracefully shutdown thread executor and make sure the queue was fully drained.
	 */
	@Override
	public void destroy() {
		try {
			tradeQueue.destroy();
			shutdownRequested.set(true);
			tradeQueueConsumer.shutdown();
			tradeQueueConsumer.awaitTermination(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("Error shutting down gracefully the executor service", e);
		}
		
		final int tradeQueueSize = tradeQueue.size();
		if (tradeQueueSize > 0) {
			final List<Trade> listReceivedtradeLeft = tradeQueue.drain(tradeQueueSize);
			tradeRepository.save(listReceivedtradeLeft);
		}
	}
	
	@Override
	public void receiveTrade(final Trade trade) {
		requireNonNull(trade, ARGUMENT_REQUIRED);
		tradeQueue.offer(trade);
		totalReceived.increment();
	}
	
	@Override
	public void receiveTrade(final List<Trade> trades) {
		requireNonNull(trades, ARGUMENT_REQUIRED);
		ForkJoinPool.commonPool().execute(() -> trades.stream().forEach(trade -> {
            receiveTrade(trade);
        }));
	}
	
	public boolean isShutdownRequested() {
		return shutdownRequested.get();
	}
	
	@ManagedOperation(description = "set batch size in which the trade queue consumer will be drained")
	public void setConsumerBatchSize(int consumerBatchSize) {
		this.consumerBatchSize.set(consumerBatchSize);
	}
	
	@ManagedAttribute(description = "batch size in which the trade queue consumer will be drained")
	public int getConsumerBatchSize() {
		return consumerBatchSize.get();
	}

	@Override
	@ManagedAttribute(description = "current total of received trade")
	public long getTotalReceived() {
		return totalReceived.sum();
	}
}
