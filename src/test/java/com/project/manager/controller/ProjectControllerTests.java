package com.project.manager.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.project.manager.model.Project;

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
	private Project project;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		this.project = mapper.readValue(this.env.getProperty("project.json"), Project.class);
		this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void testGetAllProjects() throws Exception {
		String uri = "/projectservice/projects";
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
	}

	@Test
	public void testSaveProject() throws Exception {
		String uri = "/projectservice/save";
		String inputJson = new ObjectMapper().writeValueAsString(this.project);
		MvcResult mvcResult = this.mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		// {"priority":14,"user":{"employeeId":"1","firstName":"name","id":1,"lastName":"last","projectId":0,"taskId":1},"name":"Project1","startDate":"2019-05-01","endDate":"2019-05-02"}
//	   String content = mvcResult.getResponse().getContentAsString();
//	   assertEquals(content, "Product is created successfully");
	}
}
