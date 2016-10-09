package com.github.carlosraphael.trade.consumption.service;

import java.util.List;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.service.Service;

/**
 * Define simple trade service interface 
 * @author carlos
 */
public interface TradeService extends Service {
	
	/**
	 * method takes a received trade and stores in the queue to be batch saved in the repository in background 
	 * @param trade {@link Trade}
	 */
	void receiveTrade(Trade trade);
	
	/**
	 * @param trades {@link List}
	 */
	void receiveTrade(List<Trade> trades);
	
	/**
	 * @return {@code long} current total of received trade
	 */
	long getTotalReceived();
}
