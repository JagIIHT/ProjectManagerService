package com.project.manager.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.manager.ProjectManagerServiceApplication;
import com.project.manager.model.Parent;
import com.project.manager.model.Project;
import com.project.manager.model.Task;
import com.project.manager.model.User;
import com.project.manager.service.ProjectManagerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProjectManagerServiceApplication.class)
@WebAppConfiguration
@ContextConfiguration
@TestPropertySource("/application-test.properties")
public class ProjectControllerTests {

	private MockMvc mvc;
	@Autowired
	WebApplicationContext webApplicationContext;
	@Autowired
	private Environment env;
	@MockBean
	private ProjectManagerService projectManagerService;
	ObjectMapper mapper;
	private Task task;
	private Parent parent;
	private User user;
	private Project project;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		this.mapper = new ObjectMapper();
		this.mapper.registerModule(new JavaTimeModule());

		this.parent = this.mapper.readValue(this.env.getProperty("parent.json"), Parent.class);
		this.user = this.mapper.readValue(this.env.getProperty("user.json"), User.class);
		this.project = this.mapper.readValue(this.env.getProperty("project.json"), Project.class);
		this.task = this.mapper.readValue(this.env.getProperty("task.json"), Task.class);

		List<Task> tasks = new ArrayList<Task>();
		List<Parent> parents = new ArrayList<Parent>();
		List<Project> projects = new ArrayList<Project>();
		List<User> users = new ArrayList<User>();

		tasks.add(this.task);
		parents.add(this.parent);
		projects.add(this.project);
		users.add(this.user);
		this.parent.setTasks(tasks);

		Mockito.when(this.projectManagerService.getAllTasks()).thenReturn(tasks);
		Mockito.when(this.projectManagerService.saveTask(this.task)).thenReturn(this.task);
		Mockito.when(this.projectManagerService.getTaskById(Mockito.anyString())).thenReturn(this.task);
		Mockito.when(this.projectManagerService.endTask(Mockito.anyString())).thenReturn(this.task);

		Mockito.when(this.projectManagerService.getAllParent()).thenReturn(parents);

		Mockito.when(this.projectManagerService.getProjectList()).thenReturn(projects);
		Mockito.when(this.projectManagerService.getAllUsers()).thenReturn(users);

		this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void testGetAllProjects() throws Exception {
		String uri = "/projectservice/projects";
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void testSaveProject() throws Exception {
		String uri = "/projectservice/save";
		String inputJson = this.env.getProperty("project.json");
		MvcResult mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testGetAllUsers() throws Exception {
		String uri = "/userservice/users";
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testGetAllUsers_Failure() throws Exception {
		Mockito.when(this.projectManagerService.getAllUsers()).thenReturn(new ArrayList<User>());
		String uri = "/userservice/users";
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
	}

	@Test
	public void testSaveOrUpdateUser() throws Exception {
		String uri = "/userservice/save";
		String inputJson = this.env.getProperty("user.json");
		MvcResult mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void testGetAllTasks() throws Exception {
		String uri = "/taskservice/tasks";
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void testGetAllTasks_Failure() throws Exception {
		String uri = "/taskservice/tasks";
		Mockito.when(this.projectManagerService.getAllTasks()).thenReturn(new ArrayList<Task>());
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
	}

	@Test
	public void testGetAllParent() throws Exception {
		String uri = "/taskservice/parent";
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void testGetAllParent_Failure() throws Exception {
		String uri = "/taskservice/parent";
		Mockito.when(this.projectManagerService.getAllParent()).thenReturn(new ArrayList<Parent>());
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
	}

	@Test
	public void testUpdateTask() throws Exception {
		String uri = "/taskservice/update";
		String inputJson = this.env.getProperty("task.json");
		MvcResult mvcResult = this.mvc
				.perform(
						MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void testAddTask() throws Exception {
		String uri = "/taskservice/add";
		String inputJson = this.env.getProperty("task.json");
		MvcResult mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void testAddParentTask() throws Exception {
		String uri = "/taskservice/add/parent";
		String inputJson = this.env.getProperty("parent.json");
		MvcResult mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testAddParentTask_Failure() throws Exception {
		String uri = "/taskservice/add/parent";
		String inputJson = "";
		MvcResult mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}

	@Test
	public void testUpdateEndDate() throws Exception {
		String uri = "/taskservice/endtask/1";
		MvcResult mvcResult = this.mvc
				.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testGetTask() throws Exception {
		String uri = "/taskservice/task/1";
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
}
