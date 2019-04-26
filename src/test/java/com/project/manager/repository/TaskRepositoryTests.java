package com.project.manager.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.project.manager.model.Parent;
import com.project.manager.model.Task;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTests {

	@Autowired
	private TaskRepository taskRepository;
	private Task task;

	@Before
	public void setUp() {
		Parent parent = new Parent();
		parent.setTask("Parent1");
		this.task = new Task();
		this.task.setTask("Task1");
		this.task.setPriority(3);
		this.task.setStartDate(LocalDateTime.of(2011, 01, 22, 0, 0).toLocalDate());
		this.task.setEndDate(LocalDateTime.of(2020, 01, 22, 0, 0).toLocalDate());
		this.task.setParent(parent);
	}

	@Test
	public void testAddTask() {
		Task task = this.taskRepository.save(this.task);
		assertThat(task.getTask().equalsIgnoreCase(this.task.getTask()));
	}

	@Test
	public void testFindAllTasks() {
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
	public void testFindById() {
		this.taskRepository.save(this.task);
		Task task = this.taskRepository.findById(3).get();
		assertThat(task.getTask().equalsIgnoreCase(this.task.getTask()));
	}

}
