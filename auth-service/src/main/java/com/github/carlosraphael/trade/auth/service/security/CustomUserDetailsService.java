package com.github.carlosraphael.trade.auth.service.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.carlosraphael.trade.auth.model.User;
import com.github.carlosraphael.trade.auth.repository.UserRepository;

/**
 * Basic auth service with hardcoded roles.
 * @author carlos
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final SimpleGrantedAuthority USER_ROLE = new SimpleGrantedAuthority("user");
	private static final SimpleGrantedAuthority SERVER_ROLE = new SimpleGrantedAuthority("server");
	
	private final UserRepository repository;

	@Autowired
	public CustomUserDetailsService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = repository.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), 
				true, true, true, true, Arrays.asList(USER_ROLE, SERVER_ROLE));
	}
}
