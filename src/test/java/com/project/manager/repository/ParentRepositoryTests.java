package com.project.manager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.manager.model.Parent;
import com.project.manager.model.Task;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParentRepositoryTests {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ParentTaskRepository parentRepository;
	private Task task;

	@Before
	public void setUp() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		
		Parent parent = new Parent();
		parent.setTask("Parent1");
		parent.setId(1);
		this.task = new Task();
		this.task.setTask("Task1");
		this.task.setPriority(3);
		this.task.setId(1);
		this.task.setStartDate(LocalDateTime.of(2011, 01, 22, 0, 0).toLocalDate());
		this.task.setEndDate(LocalDateTime.of(2020, 01, 22, 0, 0).toLocalDate());
		this.task.setParent(parent);
	}

	@Test
	public void testFindById() {
		Task task = this.taskRepository.save(this.task);
		Parent parent = this.parentRepository.findById(task.getParent().getId()).get();
		assertThat(parent.getTask().equalsIgnoreCase(this.task.getParent().getTask()));
	}
}
