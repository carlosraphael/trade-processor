package com.github.carlosraphael.trade.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@MappedSuperclass
public class Model implements Serializable {

	private static final long serialVersionUID = -4920839466412259185L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time", updatable = false)
	protected Date createdTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time")
	protected Date lastUpdatedTime;
	
	@PrePersist
    protected void onCreate() {
         lastUpdatedTime = createdTime = new Date();
    }
	
	@PreUpdate
    protected void onUpdate() {
         lastUpdatedTime = new Date();
    }
}
