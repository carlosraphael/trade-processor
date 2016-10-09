package com.github.carlosraphael.trade.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.test.UnitTest;
import com.github.carlosraphael.trade.util.TradeQueue;

/**
 * Simple test scenarios for {@link TradeQueue} 
 * @author carlos
 */
public class TradeQueueTest extends UnitTest {

	final TradeQueue tradeQueue = new TradeQueue(1);
	final Trade trade = generate(1).get(0);
	final Date date = new Date();
	
	@Before
	public void setup() throws Exception {
		trade.setId(1L);
		trade.setCreatedTime(date);
		
		tradeQueue.afterPropertiesSet();
	}
	
	@After
	public void tearDown() throws Exception {
		tradeQueue.destroy();
	}
	
	@Test
	public void shouldOfferTradeObjectToTheQueue() {
		assertEquals(0, tradeQueue.countWaitingProducers());
		assertEquals(0, tradeQueue.size());
		
		tradeQueue.offer(trade);
		
		LockSupport.parkNanos(MILLISECONDS.toNanos(500));
		
		assertEquals(1, tradeQueue.size());
	}
	
	@Test
	public void shouldDrainTheQueue() {
		shouldOfferTradeObjectToTheQueue();
		
		final int expectedQueueSize = tradeQueue.size();
		final List<Trade> drainedQueue = tradeQueue.drain(expectedQueueSize);
		
		assertEquals(expectedQueueSize, drainedQueue.size());
		assertEquals(date, drainedQueue.get(0).getCreatedTime());
	}
	
	@Test
	public void shouldUseFallbackQueue() {
		shouldOfferTradeObjectToTheQueue();
		
		//queue is full as its limit size is set to 1 in the test setup
		assertEquals(1, tradeQueue.size());
		
		tradeQueue.offer(new Trade());
		LockSupport.parkNanos(MILLISECONDS.toNanos(200));
		//fallback count must be 1
		assertEquals(1, tradeQueue.countFallback());
		
		//drain and check result size which must be 2
		assertEquals(2, tradeQueue.drain(2).size());
		//queue must be empty
		assertEquals(0, tradeQueue.size());
	}
}
