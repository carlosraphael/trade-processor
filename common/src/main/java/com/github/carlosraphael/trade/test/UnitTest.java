package com.github.carlosraphael.trade.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.model.enumeration.CountryCode;
import com.github.carlosraphael.trade.model.enumeration.CurrencyCode;
import com.github.javafaker.Faker;

public abstract class UnitTest {
	
	protected static List<Trade> generate(int quantity) {
		Faker faker = new Faker();
		List<Trade> list = new ArrayList<>(quantity);
		for (int i=0; i < quantity; i++) {
			Trade trade = new Trade();
			trade.setId(faker.number().randomNumber());
			trade.setUserId(faker.number().randomNumber());
			trade.setAmountBuy(BigDecimal.valueOf(faker.number().randomDouble(8, 100, 10000)));
			trade.setAmountSell(BigDecimal.valueOf(faker.number().randomDouble(8, 100, 10000)));
			trade.setRate(BigDecimal.valueOf(faker.number().randomDouble(8, 1, 10)));
			trade.setCreatedTime(faker.date().between(DateTime.now().minusDays(1).toDate(), DateTime.now().toDate()));
			trade.setLastUpdatedTime(faker.date().between(DateTime.now().minusDays(1).toDate(), DateTime.now().toDate()));
			trade.setTimePlaced(faker.date().between(DateTime.now().minusDays(1).toDate(), DateTime.now().toDate()));
			trade.setCurrencyFrom(CurrencyCode.values()[faker.number().numberBetween(0, 3)]);
			trade.setCurrencyTo(CurrencyCode.values()[faker.number().numberBetween(0, 3)]);
			trade.setOriginatingCountry(CountryCode.values()[faker.number().numberBetween(0, 2)]);
			list.add(trade);
		}
		return list;
	}
}
