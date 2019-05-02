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
import com.project.manager.model.Parent;
import com.project.manager.model.Task;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration
@TestPropertySource("/application-test.properties")
public class ParentRepositoryTests {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ParentTaskRepository parentRepository;
	@Autowired
	private Environment env;
	private Task task;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		this.task = mapper.readValue(this.env.getProperty("task.json"), Task.class);
	}

	@Test
	public void testFindById() {
		Task task = this.taskRepository.save(this.task);
		Parent parent = this.parentRepository.findById(task.getParent().getId()).get();
		assertThat(parent.getTask().equalsIgnoreCase(this.task.getParent().getTask()));
	}
}
