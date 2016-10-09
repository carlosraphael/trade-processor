package com.github.carlosraphael.trade.processor.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.model.enumeration.CurrencyCode;
import com.github.carlosraphael.trade.processor.controller.dto.TotalPairMarketDTO;
import com.github.carlosraphael.trade.processor.service.TradeProcessorService;

/**
 * Basic entrypoint which servers simple data around trading. 
 * @author carlos
 */
@RestController
@RequestMapping(value = "/trade", produces = MediaType.APPLICATION_JSON_VALUE)
public class TradeProcessorController {
	
	private final TradeProcessorService tradeProcessorService;
	
	@Autowired
	public TradeProcessorController(TradeProcessorService tradeProcessorService) {
		this.tradeProcessorService = tradeProcessorService;
	}

	@GetMapping
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity<PagedResources<Trade>> getTrade(
			@PageableDefault(size = Integer.MAX_VALUE) Pageable pageSpecification, PagedResourcesAssembler assembler) {
		
		final Page<Trade> trades = tradeProcessorService.getAll(pageSpecification);
		return new ResponseEntity<>(assembler.toResource(trades), HttpStatus.OK);
	}
	
	
	@GetMapping("/pair-market")
	public ResponseEntity<TotalPairMarketDTO> getTotalFromOneCurrencyPairMarket(@RequestAttribute CurrencyCode currencyFrom, 
			@RequestAttribute CurrencyCode currencyTo) {
		
		final long grandTotal = tradeProcessorService.getTotalCurrencyVolumeBy(currencyFrom, currencyTo);
		return new ResponseEntity<>(new TotalPairMarketDTO(grandTotal, new Date()), HttpStatus.OK);
	}
	
}