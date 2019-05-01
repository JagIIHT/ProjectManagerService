package com.project.manager.controller;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.manager.ProjectManagerServiceApplication;
import com.project.manager.model.Project;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProjectManagerServiceApplication.class)
@WebAppConfiguration
public class ProjectControllerTests {

	private MockMvc mvc;
	@Autowired
	WebApplicationContext webApplicationContext;
	private Project project;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		this.project = new Project();
		this.project.setEndDate(LocalDateTime.of(2020, 07, 01, 0, 0).toLocalDate());
		this.project.setName("Project1");
		this.project.setPriority(5);
		this.project.setStartDate(LocalDateTime.now().toLocalDate());
//		this.project.setUser(this.user);
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
				MockMvcRequestBuilders.post(uri)
	      .contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson)).andReturn();
	   
	   int status = mvcResult.getResponse().getStatus();
	   assertEquals(201, status);
	   //{"priority":14,"user":{"employeeId":"1","firstName":"name","id":1,"lastName":"last","projectId":0,"taskId":1},"name":"Project1","startDate":"2019-05-01","endDate":"2019-05-02"}
//	   String content = mvcResult.getResponse().getContentAsString();
//	   assertEquals(content, "Product is created successfully");
	}
}
