package com.project.manager.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import com.project.manager.model.Task;

@RunWith(SpringRunner.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
@TestPropertySource("/application-test.properties")
public class TaskRepositoryTests {

	@Autowired
	private TaskRepository taskRepository;
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
	public void test1_AddTask() {
		Task task = this.taskRepository.save(this.task);
		assertThat(task.getTask().equalsIgnoreCase(this.task.getTask()));
	}

	@Test
	public void test2_FindAllTasks() {
		this.taskRepository.save(this.task);
		Iterable<Task> tasks = this.taskRepository.findAll();
		boolean[] pass = { false };
		tasks.forEach(t -> {
			if (t.getTask().equalsIgnoreCase(this.task.getTask())) {
				pass[0] = true;
			}
		});
		assertTrue(pass[0]);
	}

	@Test
	public void test3_FindById() {
		Task savedTask = this.taskRepository.save(this.task);
		Task task = this.taskRepository.findById(savedTask.getId()).get();
		assertThat(task.getTask().equalsIgnoreCase(this.task.getTask()));
	}

}
