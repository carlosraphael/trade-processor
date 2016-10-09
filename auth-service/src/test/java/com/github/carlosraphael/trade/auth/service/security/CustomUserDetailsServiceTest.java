package com.github.carlosraphael.trade.auth.service.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.github.carlosraphael.trade.auth.model.User;
import com.github.carlosraphael.trade.auth.repository.UserRepository;

public class CustomUserDetailsServiceTest {

	@InjectMocks
	private CustomUserDetailsService service;

	@Mock
	private UserRepository repository;

	@Before
	public void setup() {
		initMocks(this);
	}

	@Test
	public void shouldLoadByUsernameWhenUserExists() {
		final User user = new User();
		user.setEmail("email@test.com");
		user.setPassword("password");

		when(repository.findByEmail(any())).thenReturn(user);
		UserDetails loaded = service.loadUserByUsername("email@test.com");

		assertEquals(user.getEmail(), loaded.getUsername());
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldFailToLoadByUsernameWhenUserNotExists() {
		service.loadUserByUsername("email@test.com");
	}
}