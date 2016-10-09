package com.github.carlosraphael.trade.auth.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.carlosraphael.trade.auth.AuthApplication;
import com.github.carlosraphael.trade.auth.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AuthApplication.class)
public class UserRepositoryTest {

	@Autowired
	private UserRepository repository;

	@Test
	public void shouldSaveAndFindUserByName() {
		User user = new User();
		user.setEmail("name@test.com");
		user.setPassword("password");
		repository.save(user);

		User found = repository.findByEmail(user.getEmail());
		assertEquals(user.getEmail(), found.getEmail());
		assertEquals(user.getPassword(), found.getPassword());
	}
}
