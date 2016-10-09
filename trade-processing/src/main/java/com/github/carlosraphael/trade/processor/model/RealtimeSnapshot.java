package com.github.carlosraphael.trade.processor.model;

import java.io.Serializable;

import javax.persistence.Entity;

import com.github.carlosraphael.trade.model.Model;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class RealtimeSnapshot extends Model implements Serializable {
	
	private static final long serialVersionUID = -879528865037368050L;

	private long tradeVolumeMax;
	private long tradeVolumeMin;
	private double tradeVolumeMean;

	private double amountSellMax;
	private double amountSellMin;
	private double amountSellMean;
	
	private double amountBuyMax;
	private double amountBuyMin;
	private double amountBuyMean;
}
