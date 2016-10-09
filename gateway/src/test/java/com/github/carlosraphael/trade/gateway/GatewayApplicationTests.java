package com.github.carlosraphael.trade.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.carlosraphael.trade.gateway.GatewayApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GatewayApplication.class)
@WebAppConfiguration
public class GatewayApplicationTests {

	@Test
	public void contextLoads() {
	}
}
