package com.github.carlosraphael.trade.processor.service;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SlidingTimeWindowReservoir;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.github.carlosraphael.trade.model.Trade;
import com.github.carlosraphael.trade.processor.model.RealtimeSnapshot;
import com.github.carlosraphael.trade.processor.repository.RealtimeSnapshotRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Attempt to implement a very basic "real-time" metrics around trade processor by using histograms to track trade volume
 * and amount sell/buy.
 * 
 * The whole histogram time(1h) is optionally configurable as well as the time interval(1s) between every snapshot.
 * 
 * @author carlos
 *
 */
@Slf4j
@Service
public class RealtimeProcessorServiceImpl implements RealtimeProcessorService {

    private final MetricRegistry metricRegistry = new MetricRegistry();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
	private final ScheduledExecutorService snapshotExecutorService = Executors.newScheduledThreadPool(1);
	private final RealtimeSnapshotRepository realtimeSnapshotRepository;

	private volatile RealtimeSnapshot currentRealtimeSnapshot;
	
	@Value("${trade.processing.realtime.sliding.time.window-in-seconds:3600}")
	private int slidingTimeWindowInSeconds = 3600;
	@Value("${trade.processing.realtime.time.interval.snapshot-in-seconds:3}")
	private int timeIntervalToSnapshotInSeconds = 3;
	private JmxReporter jmxReporter;
	
	@Autowired
	public RealtimeProcessorServiceImpl(RealtimeSnapshotRepository realtimeSnapshotRepository) {
		this.realtimeSnapshotRepository = realtimeSnapshotRepository;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
		jmxReporter.start();
		
		//basic metrics ->
		metricRegistry.register("amountBuy", 
				new Histogram(new SlidingTimeWindowReservoir(slidingTimeWindowInSeconds, SECONDS)));
		
		metricRegistry.register("amountSell", 
				new Histogram(new SlidingTimeWindowReservoir(slidingTimeWindowInSeconds, SECONDS)));
		
		metricRegistry.register("tradeVolume", 
				new Histogram(new SlidingTimeWindowReservoir(slidingTimeWindowInSeconds, SECONDS)));
		
		snapshotExecutorService.scheduleWithFixedDelay(this::snapshot, 2, timeIntervalToSnapshotInSeconds, SECONDS);
	}
	
	@Override
	public void destroy() throws Exception {
		jmxReporter.stop();
		executorService.shutdown();
		executorService.awaitTermination(2, SECONDS);
		snapshotExecutorService.shutdown();
		snapshotExecutorService.awaitTermination(2, SECONDS);
	}
	
	@Override
	public void process(List<Trade> trades) {
		metricRegistry.histogram("tradeVolume").update(trades.size());
		
		executorService.execute(() -> trades.stream().forEach(trade -> {
            final BigDecimal amountBuy = trade.getAmountBuy();
            if (amountBuy != null && !amountBuy.equals(BigDecimal.ZERO))
                metricRegistry.histogram("amountBuy").update(amountBuy.longValue()); // losing precision

            final BigDecimal amountSell = trade.getAmountSell();
            if (amountSell != null && !amountSell.equals(BigDecimal.ZERO))
                metricRegistry.histogram("amountSell").update(amountSell.longValue()); // losing precision
        }));
	}
	
	private void snapshot() {
		final Timer timer = metricRegistry.timer("snapshotExecutionTime");
		final Context context = timer.time();
		try {
			final RealtimeSnapshot realtimeSnapshot = new RealtimeSnapshot();

			final Snapshot snapshotTradeVolume = metricRegistry.histogram("tradeVolume").getSnapshot();
			final Snapshot snapshotAmountBuy = metricRegistry.histogram("amountBuy").getSnapshot();
			final Snapshot snapshotAmountSell = metricRegistry.histogram("amountSell").getSnapshot();
			
			realtimeSnapshot.setTradeVolumeMax(snapshotTradeVolume.getMax());
			realtimeSnapshot.setTradeVolumeMean(snapshotTradeVolume.getMean());
			realtimeSnapshot.setTradeVolumeMin(snapshotTradeVolume.getMin());
			
			realtimeSnapshot.setAmountBuyMax(snapshotAmountBuy.getMax());
			realtimeSnapshot.setAmountBuyMean(snapshotAmountBuy.getMean());
			realtimeSnapshot.setAmountBuyMin(snapshotAmountBuy.getMin());
			
			realtimeSnapshot.setAmountSellMax(snapshotAmountSell.getMax());
			realtimeSnapshot.setAmountSellMean(snapshotAmountSell.getMean());
			realtimeSnapshot.setAmountSellMin(snapshotAmountSell.getMin());
			
			this.currentRealtimeSnapshot = realtimeSnapshot;
			realtimeSnapshotRepository.save(realtimeSnapshot);
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
		} finally {
			context.stop();
		}
	}
	
	@Override
	public RealtimeSnapshot getSnapshot() {
		return currentRealtimeSnapshot;
	}

	@Cacheable
	@Override
	public List<RealtimeSnapshot> getSnapshotHistory(Date start, Date end) {
		return realtimeSnapshotRepository.findAllByCreatedTimeBetween(start, end);
	}
	
	public int getTimeIntervalToSnapshotInSeconds() {
		return timeIntervalToSnapshotInSeconds;
	}
}
