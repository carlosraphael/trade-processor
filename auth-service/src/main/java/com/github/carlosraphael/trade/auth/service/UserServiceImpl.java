package com.github.carlosraphael.trade.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.carlosraphael.trade.auth.model.User;
import com.github.carlosraphael.trade.auth.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private UserRepository repository;

	@Override
	public void create(User user) {

		User existing = repository.findByEmail(user.getEmail());
		Assert.isNull(existing, "user already exists: " + user.getEmail());

		String hash = encoder.encode(user.getPassword());
		user.setPassword(hash);

		repository.save(user);

		if (log.isInfoEnabled())
			log.info("new user has been created: {}", user.getEmail());
	}
}
