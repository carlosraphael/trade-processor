package com.github.carlosraphael.trade.service.client;

import java.security.Principal;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Auth service client to retrive user info
 * @author carlos
 */
@FeignClient("auth-service")
public interface AuthServiceClient {
	@RequestMapping(value = "/uaa/users/current", method = RequestMethod.GET)
	public Principal getUser(Principal principal);
}