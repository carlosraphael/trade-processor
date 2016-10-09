package com.github.carlosraphael.trade.model.enumeration;

public enum CountryCode {
	FR, IE, GB, CZ;
	
	public static CountryCode get(String countryCode) {
		try {
			return CountryCode.valueOf(countryCode);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(String.format("Country %s not supported yet", countryCode));
		}
	}
}
