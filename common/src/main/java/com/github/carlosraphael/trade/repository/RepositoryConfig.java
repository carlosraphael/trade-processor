package com.github.carlosraphael.trade.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.github.carlosraphael.trade.repository")
@EntityScan("com.github.carlosraphael.trade.model")
@EnableTransactionManagement
public class RepositoryConfig {
}
