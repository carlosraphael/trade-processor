package com.github.carlosraphael.trade.consumption.controller;

import static org.springframework.http.HttpStatus.ACCEPTED;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.carlosraphael.trade.consumption.service.TradeService;
import com.github.carlosraphael.trade.model.Trade;

/**
 * Main entry point for receiving trade messages.
 * @author carlos
 *
 */
@RestController
public class TradeConsumptionController {
	
	private final TradeService tradeService;
	
	@Autowired
	public TradeConsumptionController(TradeService tradeService) {
		this.tradeService = tradeService;
	}
	
	@PostMapping("/trades")
	public ResponseEntity<Void> httpBatchReceive(@RequestBody @Valid List<Trade> trades) {
		tradeService.receiveTrade(trades);
		return new ResponseEntity<>(ACCEPTED);
	}
	
	@PostMapping("/trade")
	public ResponseEntity<Void> httpReceive(@RequestBody @Valid Trade trade) {
		tradeService.receiveTrade(trade);
		return new ResponseEntity<>(ACCEPTED);
	}
}
