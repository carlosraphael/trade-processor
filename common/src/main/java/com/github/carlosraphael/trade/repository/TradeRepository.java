package com.github.carlosraphael.trade.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.model.enumeration.CountryCode;
import com.github.carlosraphael.trade.model.enumeration.CurrencyCode;

/**
 * Spring JPA Repository. Some useful queries, using 'query method' strategy, are defined below
 * @author carlos
 */
@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

	/**
	 * Get the total volume of trades by originating country
	 * @param originatingCountry - {@link CountryCode}
	 * @return {@code long}
	 */
	long countByOriginatingCountry(CountryCode originatingCountry);
	
	/**
	 * Get the total volume of trades by time placed
	 * @param start - {@link Date}
	 * @param end - {@link Date}
	 * @return {@code long}
	 */
	long countByTimePlacedBetween(Date start, Date end);
	
	/**
	 * Find all trades by time placed between interval. 
	 * @param start - {@link Date}
	 * @param end - {@link Date}
	 * @param page - {@link Pageable}
	 * @return {@link Page}
	 */
	Page<Trade> findAllByTimePlacedBetween(Date start, Date end, Pageable page);
	
	/**
	 * Find top 50 biggest ordered by whtatever you want (e.g. amount sell/buy)
	 * @param sort - {@link Sort} can be used to order by either amountSell, amountBuy or whatever
	 * @return {@link List}
	 */
	List<Trade> findTop50By(Sort sort);
	
	
	/**
	 * Get the total volume of trades from one particular currency pair market 
	 * @param currencyFrom - {@link CurrencyCode}
	 * @param currencyTo - {@link CurrencyCode}
	 * @return {@code long}
	 */
	long countByCurrencyFromAndCurrencyTo(CurrencyCode currencyFrom, CurrencyCode currencyTo);
	
	/**
	 * Find top 100 biggest trades from one particular currency pair market ordered by amount sell/buy or whatever 
	 * @param currencyFrom - {@link CurrencyCode}
	 * @param currencyTo - {@link CurrencyCode}
	 * @param sort - {@link Sort} can be used to order by either amountSell, amountBuy or whatever
	 * @return {@link List}
	 */
	List<Trade> findTop100ByCurrencyFromAndCurrencyTo(CurrencyCode currencyFrom, CurrencyCode currencyTo, Sort sort);
	
	/**
	 * Find all trades from one particular currency pair market between a range of time placed
	 * @param currencyFrom - {@link CurrencyCode}
	 * @param currencyTo - {@link CurrencyCode}
	 * @param start - {@link Date}
	 * @param end - {@link Date}
	 * @param sort - {@link Sort} can be used to order by either amountSell, amountBuy or whatever
	 * @return {@link List}
	 */
	List<Trade> findByCurrencyFromAndCurrencyToAndTimePlacedBetween(CurrencyCode currencyFrom, 
			CurrencyCode currencyTo, Date start, Date end, Sort sort);
	
	/**
	 * Find top 100 biggest trade from a particular country ordered by amount(sell/buy) or whatever
	 * @param originatingCountry
	 * @param sort - {@link Sort} can be used to order by either amountSell, amountBuy or whatever
	 * @return {@link List}
	 */
	List<Trade> findTop100ByOriginatingCountry(CountryCode originatingCountry, Sort sort);
	
	/**
	 * Find top 10 trades by user
	 * @param userId - {@link Long}
	 * @param sort - {@link Sort} can be used to order by either amountSell, amountBuy or whatever
	 * @return {@link List}
	 */
	List<Trade> findTop10ByUserId(Long userId, Sort sort);
}