package com.github.carlosraphael.trade.processor.controller;

import static org.springframework.http.HttpStatus.ACCEPTED;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.processor.model.RealtimeSnapshot;
import com.github.carlosraphael.trade.processor.service.RealtimeProcessorService;

/**
 * Controller to get batch received trades, from trade consumption service, and then process them to get simple snapshots
 * @author carlos
 *
 */
@RestController
@RequestMapping(value = "/trade", produces = MediaType.APPLICATION_JSON_VALUE)
public class RealtimeProcessorController {

	private final RealtimeProcessorService realtimeProcessorService;
	
	@Autowired
	public RealtimeProcessorController(RealtimeProcessorService realtimeProcessorService) {
		this.realtimeProcessorService = realtimeProcessorService;		
	}
	
	@PostMapping
	public ResponseEntity<Void> batchProcessForRealtime(@RequestBody List<Trade> trades) {
		realtimeProcessorService.process(trades);
		return new ResponseEntity<>(ACCEPTED);
	}
	
	@MessageMapping("real-time")
	public RealtimeSnapshot realtimeTradeSnapshot() {
		return realtimeProcessorService.getSnapshot();
	}
	
	@MessageMapping("real-time/history")
	public List<RealtimeSnapshot> snapshotHistory(Date start, Date end) {
		return realtimeProcessorService.getSnapshotHistory(start, end);
	}
}
