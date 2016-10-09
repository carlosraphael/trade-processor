package com.github.carlosraphael.trade.processor.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.model.enumeration.CurrencyCode;
import com.github.carlosraphael.trade.repository.TradeRepository;

/**
 * Very simple service just bypass to repository. so no unit test for this guy. 
 * @author carlos
 *
 */
@Service
public class TradeProcessorServiceImpl implements TradeProcessorService {

	private TradeRepository tradeRepository;
	
	@Autowired
	public TradeProcessorServiceImpl(TradeRepository tradeRepository) {
		this.tradeRepository = tradeRepository;
	}
	
	@Override
	public List<Trade> getTop50SellTransaction() {
		return tradeRepository.findTop50By(new Sort(Sort.Direction.DESC, "amountSell"));
	}

	@Override
	public List<Trade> getTop50BuyTransaction() {
		return tradeRepository.findTop50By(new Sort(Sort.Direction.DESC, "amountBuy"));
	}

	@Override
	public Page<Trade> getAllBasedOnTimePlaced(Date start, Date end, Pageable page) {
		return tradeRepository.findAllByTimePlacedBetween(start, end, page);
	}
	
	@Override
	public Page<Trade> getAll(Pageable page) {
		return tradeRepository.findAll(page);
	}

	@Override
	public long getTotalCurrencyVolumeBy(CurrencyCode currencyFrom, CurrencyCode currencyTo) {
		return tradeRepository.countByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo);
	}
}
