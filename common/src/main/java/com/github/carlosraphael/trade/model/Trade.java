package com.github.carlosraphael.trade.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.github.carlosraphael.trade.model.enumeration.CountryCode;
import com.github.carlosraphael.trade.model.enumeration.CurrencyCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Trade extends Model {

	private static final long serialVersionUID = 7250394152535779522L;

	@NotNull
	private Long userId;
	@NotNull @Enumerated(EnumType.STRING)
	private CurrencyCode currencyFrom;
	@NotNull @Enumerated(EnumType.STRING)
	private CurrencyCode currencyTo;
	@NotNull
	private BigDecimal amountSell;
	@NotNull
	private BigDecimal amountBuy;
	@NotNull
	private BigDecimal rate;
	@NotNull @Temporal(TemporalType.TIMESTAMP)
	private Date timePlaced;
	@NotNull @Enumerated(EnumType.STRING)
	private CountryCode originatingCountry;
}
