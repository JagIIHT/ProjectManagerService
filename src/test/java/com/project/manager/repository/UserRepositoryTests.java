package com.project.manager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.manager.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration
@TestPropertySource("/application-test.properties")
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private Environment env;
	private User user;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		this.user = mapper.readValue(this.env.getProperty("user.json"), User.class);
	}

	@Test
	public void test1_SaveOrUpdate() {
		User savedUser = this.userRepository.save(this.user);
		assertThat(this.user.getFirstName().equalsIgnoreCase(savedUser.getFirstName()));
	}

	@Test
	public void test2_FindById() {
		User savedUser = this.userRepository.save(this.user);
		User user = this.userRepository.findOneUserByProjectId(String.valueOf(savedUser.getProjectId())).get(0);
		assertThat(user.getFirstName().equalsIgnoreCase(this.user.getFirstName()));
	}
}
