package com.github.carlosraphael.trade.processor;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.carlosraphael.trade.repository.RepositoryConfig;
import com.github.carlosraphael.trade.security.SecurityConfig;
import com.github.carlosraphael.trade.ws.WebSocketConfig;

@SpringBootApplication
@EnableWebMvc
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import({RepositoryConfig.class, WebSocketConfig.class, SecurityConfig.class})
@EnableJpaRepositories("com.github.carlosraphael.trade.processor.repository")
@EntityScan("com.github.carlosraphael.trade.processor.model")
@ComponentScan({"com.github.carlosraphael.trade.util", "com.github.carlosraphael.trade.processor"})
public class ProcessorApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(ProcessorApplication.class, args);
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder().simpleDateFormat("dd-MMM-yy hh:mm:ss");
		converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
	}
}
