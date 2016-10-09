package com.github.carlosraphael.trade.processor.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.carlosraphael.trade.processor.model.RealtimeSnapshot;

@Repository
public interface RealtimeSnapshotRepository extends JpaRepository<RealtimeSnapshot, Long> {
	
	List<RealtimeSnapshot> findAllByCreatedTimeBetween(Date start, Date end);
}
