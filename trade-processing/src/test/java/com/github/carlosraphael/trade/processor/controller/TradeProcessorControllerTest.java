package com.github.carlosraphael.trade.processor.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.github.carlosraphael.trade.processor.TestConfig;
import com.github.carlosraphael.trade.repository.TradeRepository;
import com.github.carlosraphael.trade.test.WebIntegrationTest;

/**
 * Simple integration test for the frontend entrypoint.
 * 
 * TODO -> implement more consistent scenarios
 *  
 * @author carlos
 */
@SpringBootTest(classes = TestConfig.class)
public class TradeProcessorControllerTest extends WebIntegrationTest {

	@Autowired
	private TradeRepository tradeRepository;
	
	@Before
	public void setup() {
		super.setup();
		tradeRepository.deleteAllInBatch();
		tradeRepository.save(generate(50));
	}
	
	@Test
	public void shouldGetTrade() throws Exception {
		MvcResult result = mockMvc.perform(
				get("/trade").
				accept(MediaType.APPLICATION_JSON)).
				andExpect(status().isOk()).andReturn();
		
		String json = result.getResponse().getContentAsString();
		
		// very stupid assert, it could be used 'json path' or something fancy to easily check the real json content
		assertTrue(json.contains("userId"));
	}
}
