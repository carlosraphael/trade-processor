package com.github.carlosraphael.trade.processor.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.model.enumeration.CurrencyCode;

/**
 * Trade trade processor interface. This interface defines offline operation, that is, operations which go to the
 * database or cache.
 * 
 * @author carlos
 */
public interface TradeProcessorService {

	List<Trade> getTop50SellTransaction();
	List<Trade> getTop50BuyTransaction();
	Page<Trade> getAllBasedOnTimePlaced(Date start, Date end, Pageable page);
	Page<Trade> getAll(Pageable page);
	long getTotalCurrencyVolumeBy(CurrencyCode currencyFrom, CurrencyCode currencyTo);
}
