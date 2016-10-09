package com.github.carlosraphael.trade.consumption;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.carlosraphael.trade.repository.RepositoryConfig;
import com.github.carlosraphael.trade.security.SecurityConfig;
import com.github.carlosraphael.trade.service.client.FeignClientsConfig;

@SpringBootApplication
@EnableWebMvc
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import({RepositoryConfig.class, FeignClientsConfig.class, SecurityConfig.class})
@ComponentScan({"com.github.carlosraphael.trade.util", "com.github.carlosraphael.trade.consumption"})
public class ConsumptionApplication extends WebMvcConfigurerAdapter {
	
	public static void main(String[] args) {
		SpringApplication.run(ConsumptionApplication.class, args);
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder().simpleDateFormat("dd-MMM-yy hh:mm:ss");
		converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
	}
}
