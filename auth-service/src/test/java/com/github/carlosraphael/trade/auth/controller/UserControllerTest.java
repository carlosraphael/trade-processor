package com.github.carlosraphael.trade.auth.controller;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.github.carlosraphael.trade.auth.TestConfig;
import com.github.carlosraphael.trade.auth.model.User;
import com.github.carlosraphael.trade.auth.service.UserService;
import com.github.carlosraphael.trade.test.WebIntegrationTest;
import com.sun.security.auth.UserPrincipal;

@SuppressWarnings("restriction")
@SpringBootTest(classes = TestConfig.class)
public class UserControllerTest extends WebIntegrationTest {

	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void shouldCreateNewUser() throws Exception {
		final User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("password");

		String json = json(user);

		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldFailWhenUserIsNotValid() throws Exception {
		final User user = new User();
		user.setEmail("t");
		user.setPassword("p");

		mockMvc.perform(post("/users"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnCurrentUser() throws Exception {
		mockMvc.perform(get("/users/current").principal(new UserPrincipal("test@test.com")))
				.andExpect(jsonPath("$.name").value("test@test.com"))
				.andExpect(status().isOk());
	}
}
