package com.github.carlosraphael.trade.processor.service;

import java.util.Date;
import java.util.List;

import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.processor.model.RealtimeSnapshot;
import com.github.carlosraphael.trade.service.Service;

/**
 * Define realtime processing interface
 *  
 * @author carlos
 */
public interface RealtimeProcessorService extends Service {

	void process(List<Trade> trades);
	RealtimeSnapshot getSnapshot();
	List<RealtimeSnapshot> getSnapshotHistory(Date start, Date end);
}
