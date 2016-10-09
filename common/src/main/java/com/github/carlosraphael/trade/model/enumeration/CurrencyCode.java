package com.github.carlosraphael.trade.model.enumeration;

public enum CurrencyCode {
	EUR, GBP, USD, CAD, AUD;
	
	public static CurrencyCode get(String currencyCode) {
		try {
			return CurrencyCode.valueOf(currencyCode);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(String.format("Currency %s not supported yet", currencyCode));
		}
	}
}
