package com.github.carlosraphael.trade.processor.controller.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalPairMarketDTO implements Serializable {
	private static final long serialVersionUID = -6057327282397540248L;
	private long total;
	private Date time;
}
