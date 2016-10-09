package com.github.carlosraphael.trade.processor.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.github.carlosraphael.trade.processor.TestConfig;
import com.github.carlosraphael.trade.processor.model.RealtimeSnapshot;
import com.github.carlosraphael.trade.repository.TradeRepository;
import com.github.carlosraphael.trade.test.WebIntegrationTest;

@SpringBootTest(classes = TestConfig.class)
public class RealtimeProcessorControllerTest extends WebIntegrationTest {

	final int tradeVolume = 50;
	
	@Autowired
	private TradeRepository tradeRepository;
	@Autowired
	private RealtimeProcessorController realtimeProcessorController;

	@Before
	@Override
	public void setup() {
		super.setup();
		tradeRepository.deleteAll();
	}
	
	@Test
	public void shouldBatchProcessForRealtimeAndThenTakeSnapshot() throws Exception {
		mockMvc.perform(
				post("/trade")
				.content(json(generate(tradeVolume)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
		
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10)); // FIXME
		
		final RealtimeSnapshot realtimeTradeSnapshot = realtimeProcessorController.realtimeTradeSnapshot();
		assertNotNull(realtimeTradeSnapshot);
		assertEquals(tradeVolume, realtimeTradeSnapshot.getTradeVolumeMax());
		
		final List<RealtimeSnapshot> snapshotHistory = 
				realtimeProcessorController.snapshotHistory(DateTime.now().minusHours(1).toDate(), new Date());
		
		assertNotNull(snapshotHistory);
		assertFalse(snapshotHistory.isEmpty());
	}
}
