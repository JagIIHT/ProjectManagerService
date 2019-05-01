package com.project.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.project.manager.model.Parent;
import com.project.manager.model.Task;
import com.project.manager.repository.ParentTaskRepository;
import com.project.manager.repository.TaskRepository;

@RunWith(SpringRunner.class)
public class ProjectManagerServiceTests {

	@TestConfiguration
	static class LibraryServiceTestContextConfiguration {

		@Bean
		public ProjectManagerService taskService() {
			return new ProjectManagerServiceImpl();
		}
	}

	@Autowired
	private ProjectManagerService projectManagerService;
	@MockBean
	private TaskRepository taskRepository;
	@MockBean
	private ParentTaskRepository parentRepository;
	private Task task;

	@Before
	public void setUp() {
		Parent parent = new Parent();
		List<Task> tasks = new ArrayList<Task>();

		parent.setTask("Parent1");
		this.task = new Task();
		this.task.setTask("Task1");
		this.task.setPriority(3);
		this.task.setId(1);
		this.task.setStartDate(LocalDateTime.of(2011, 01, 22, 0, 0).toLocalDate());
		this.task.setEndDate(LocalDateTime.of(2070, 01, 22, 0, 0).toLocalDate());
		this.task.setParent(parent);
		tasks.add(this.task);

		Mockito.when(this.taskRepository.findAll()).thenReturn(tasks);
		Mockito.when(this.taskRepository.save(this.task)).thenReturn(this.task);
		Mockito.when(this.taskRepository.findById(1)).thenReturn(Optional.of(this.task));
	}

	@Test
	public void testGetAllTasks() {
		List<Task> tasks = this.projectManagerService.getAllTasks();
		boolean[] pass = { false };
		tasks.forEach(t -> {
			if (t.getTask().equalsIgnoreCase(this.task.getTask())) {
				pass[0] = true;
			}
		});
		assertTrue(pass[0]);
	}

	@Test
	public void testGetTaskById() {
		Task task = this.projectManagerService.saveTask(this.task);
		Task taskFound = this.projectManagerService.getTaskById(String.valueOf(task.getId()));
		assertThat(taskFound.getTask().equalsIgnoreCase(this.task.getTask()));
	}
	
	@Test
	public void testSaveTask() {
		Task task = this.projectManagerService.saveTask(this.task);
		assertThat(task.getTask().equalsIgnoreCase(this.task.getTask()));
	}
	
	@Test
	public void testEndTask() {
		Task task = this.projectManagerService.saveTask(this.task);
		Task taskEnded = this.projectManagerService.endTask(String.valueOf(task.getId()));
		LocalDate currentDate = LocalDate.now();
		assertThat(currentDate.compareTo(taskEnded.getEndDate()) == 0);
	}
}
