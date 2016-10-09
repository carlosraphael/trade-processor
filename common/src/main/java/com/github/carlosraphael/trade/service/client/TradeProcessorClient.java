package com.github.carlosraphael.trade.service.client;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.carlosraphael.trade.model.Trade;

/**
 * Trade processor client for realtime processing.
 * 
 * It could be configured a fallback in case of trade processor is not responding
 * 
 * @author carlos
 */
@FeignClient("trade-processing")
public interface TradeProcessorClient {
	@RequestMapping(value = "/processing/trade", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Void> batchProcessForRealtime(@RequestBody @Valid List<Trade> trades);
}