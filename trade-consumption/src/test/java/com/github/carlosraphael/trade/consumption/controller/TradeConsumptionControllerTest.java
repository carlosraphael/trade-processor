package com.github.carlosraphael.trade.consumption.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.github.carlosraphael.trade.consumption.TestConfig;
import com.github.carlosraphael.trade.repository.TradeRepository;
import com.github.carlosraphael.trade.test.WebIntegrationTest;

/**
 * Happy days web integration test.
 * 
 * >> TODO: implement different scenarios <<
 *  
 * @author carlos
 */
@SpringBootTest(classes = TestConfig.class)
public class TradeConsumptionControllerTest extends WebIntegrationTest {

	@Autowired
	private TradeRepository tradeRepository;
	
	@Before
	@Override
	public void setup() {
		super.setup();
		tradeRepository.deleteAll();
	}
	
	@Test
	public void shouldReceiveTrade() throws Exception {
		final int tradeVolume = 1;
		
		mockMvc.perform(
				post("/trade")
				.content(json(generate(tradeVolume).get(0)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
		
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
		
		Assert.assertEquals(tradeVolume, tradeRepository.count());
	}
	
	@Test
	public void shouldBatchReceiveTrade() throws Exception {
		final int tradeVolume = 2;
		
		mockMvc.perform(
				post("/trades")
				.content(json(generate(tradeVolume)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
		
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
		
		Assert.assertEquals(tradeVolume, tradeRepository.count());
	}
}
