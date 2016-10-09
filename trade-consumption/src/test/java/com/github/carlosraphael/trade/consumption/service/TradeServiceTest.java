package com.github.carlosraphael.trade.consumption.service;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.concurrent.locks.LockSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.carlosraphael.trade.repository.TradeRepository;
import com.github.carlosraphael.trade.service.client.TradeProcessorClient;
import com.github.carlosraphael.trade.test.UnitTest;
import com.github.carlosraphael.trade.util.TradeQueue;

/**
 * FIXME
 * @author carlos
 */
public class TradeServiceTest extends UnitTest {

	TradeServiceImpl tradeService;
	TradeQueue tradeQueue;
	
	@Before
	public void setup() throws Exception {
		tradeQueue = new TradeQueue();
		tradeQueue.afterPropertiesSet();
		
		tradeService = new TradeServiceImpl(mock(TradeRepository.class), tradeQueue, mock(TradeProcessorClient.class));
		tradeService.afterPropertiesSet();
	}
	
	@After
	public void tearDown() {
		tradeService.destroy();
	}
	
	@Test
	public void shouldReceiveTrade() throws InterruptedException {
		assertEquals(0, tradeService.getTotalReceived());
		
		tradeService.receiveTrade(generate(1).get(0));
		assertEquals(1, tradeService.getTotalReceived());
		
		LockSupport.parkNanos(MILLISECONDS.toNanos(100));
		assertEquals(0, tradeQueue.size());
	}
}
