package com.github.carlosraphael.trade.processor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ProcessorApplication.class)
@ComponentScan("com.github.carlosraphael.trade.processor")
public class TestConfig {
}
