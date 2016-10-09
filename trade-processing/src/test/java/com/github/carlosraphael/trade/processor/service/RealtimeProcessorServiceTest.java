package com.github.carlosraphael.trade.processor.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.carlosraphael.trade.processor.model.RealtimeSnapshot;
import com.github.carlosraphael.trade.processor.repository.RealtimeSnapshotRepository;
import com.github.carlosraphael.trade.test.UnitTest;

/**
 * Only happy days
 * 
 * @author carlos
 *
 */
public class RealtimeProcessorServiceTest extends UnitTest {

	RealtimeProcessorServiceImpl realtimeProcessorService;
	
	@Before
	public void setup() throws Exception {
		realtimeProcessorService = new RealtimeProcessorServiceImpl(mock(RealtimeSnapshotRepository.class));
		realtimeProcessorService.afterPropertiesSet();
	}
	
	@After
	public void tearDown() throws Exception {
		realtimeProcessorService.destroy();
	}
	
	@Test
	public void shouldBatchProcess() throws InterruptedException {
		assertNull(realtimeProcessorService.getSnapshot());
		
		final int tradeVolume = 1000;
		
		realtimeProcessorService.process(generate(tradeVolume));
		
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(realtimeProcessorService.getTimeIntervalToSnapshotInSeconds()));
		
		final RealtimeSnapshot snapshot = realtimeProcessorService.getSnapshot();
		assertNotNull(snapshot);
		assertEquals(tradeVolume, snapshot.getTradeVolumeMax());
	}
}
