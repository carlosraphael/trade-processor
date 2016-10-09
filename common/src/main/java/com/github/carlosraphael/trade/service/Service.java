package com.github.carlosraphael.trade.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Base interface for service layer which implements {@link InitializingBean} and {@link DisposableBean} 
 * @author carlos
 */
public interface Service extends InitializingBean, DisposableBean {
}
