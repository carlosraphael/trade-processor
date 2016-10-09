package com.github.carlosraphael.trade.service.client;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.github.carlosraphael.trade.service.client")
@EnableFeignClients
public class FeignClientsConfig {
}
