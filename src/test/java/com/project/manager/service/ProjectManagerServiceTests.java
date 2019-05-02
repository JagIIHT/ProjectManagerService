package com.project.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
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
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.manager.model.Parent;
import com.project.manager.model.Project;
import com.project.manager.model.Task;
import com.project.manager.model.User;
import com.project.manager.repository.ParentTaskRepository;
import com.project.manager.repository.ProjectRepository;
import com.project.manager.repository.TaskRepository;
import com.project.manager.repository.UserRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource("/application-test.properties")
public class ProjectManagerServiceTests {

	@Autowired
	private Environment env;

	@TestConfiguration
	static class ProjectManagerServiceTestsContextConfiguration {

		@Bean
		public ProjectManagerService projectManagerService() {
			return new ProjectManagerServiceImpl();
		}
	}

	@Autowired
	private ProjectManagerService projectManagerService;
	@MockBean
	private TaskRepository taskRepository;
	@MockBean
	private ParentTaskRepository parentRepository;
	@MockBean
	private ProjectRepository projectRepository;
	@MockBean
	private UserRepository userRepository;
	private Task task;
	private Parent parent;
	private User user;
	private Project project;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		this.parent = mapper.readValue(this.env.getProperty("parent.json"), Parent.class);
		this.user = mapper.readValue(this.env.getProperty("user.json"), User.class);
		this.project = mapper.readValue(this.env.getProperty("project.json"), Project.class);
		this.task = mapper.readValue(this.env.getProperty("task.json"), Task.class);

		List<Task> tasks = new ArrayList<Task>();
		List<Parent> parents = new ArrayList<Parent>();
		List<Project> projects = new ArrayList<Project>();
		List<User> users = new ArrayList<User>();
		
		tasks.add(this.task);
		parents.add(this.parent);
		projects.add(this.project);
		users.add(this.user);
		this.parent.setTasks(tasks);

		Mockito.when(this.taskRepository.findAll()).thenReturn(tasks);
		Mockito.when(this.taskRepository.save(this.task)).thenReturn(this.task);
		Mockito.when(this.taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(this.task));

		Mockito.when(this.parentRepository.findAll()).thenReturn(parents);
		Mockito.when(this.parentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(this.parent));

		Mockito.when(this.projectRepository.findAll()).thenReturn(projects);
		Mockito.when(this.userRepository.findOneUserByProjectId(Mockito.anyString())).thenReturn(users);
		Mockito.when(this.userRepository.findAll()).thenReturn(users);
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
	public void testGetAllParent() {
		List<Parent> parents = this.projectManagerService.getAllParent();
		boolean[] pass = { false };
		parents.forEach(p -> {
			if (p.getTask().equalsIgnoreCase(this.parent.getTask())) {
				pass[0] = true;
			}
		});

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

	@Test
	public void testEndTask_Scenario1() {
		Task task = this.projectManagerService.saveTask(this.task);
		Task taskEnded = this.projectManagerService.endTask(String.valueOf(task.getId()));
		LocalDate currentDate = LocalDate.now();
		assertThat(currentDate.compareTo(taskEnded.getEndDate()) == 0);
	}

	@Test(expected = Test.None.class)
	public void testSaveParentTask() {
		this.projectManagerService.saveParentTask(this.task);
	}

	@Test(expected = Test.None.class)
	public void testSaveOrUpdateUser() {
		this.projectManagerService.saveOrUpdateUser(this.user);
	}

	@Test(expected = Test.None.class)
	public void testSaveOrUpdateProject() {
		this.projectManagerService.saveOrUpdateProject(this.project);
	}

	@Test
	public void testGetProjectList() {
		List<Project> projects = this.projectManagerService.getProjectList();
		boolean[] pass = { false };
		projects.forEach(p -> {
			if (p.getName().equalsIgnoreCase(this.project.getName())) {
				pass[0] = true;
			}
		});
		assertThat(pass[0] == true);
	}

	@Test
	public void testGetAllUsers() {
		List<User> users = this.projectManagerService.getAllUsers();
		boolean[] pass = { false };
		users.forEach(u -> {
			if (u.getEmployeeId().equalsIgnoreCase(this.user.getEmployeeId())) {
				pass[0] = true;
			}
		});
		assertThat(pass[0] == true);
	}
}
