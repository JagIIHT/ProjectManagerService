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
import com.project.manager.model.Project;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration
@TestPropertySource("/application-test.properties")
public class ProjectRepositoryTests {

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private Environment env;
	private Project project;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		this.project = mapper.readValue(this.env.getProperty("project.json"), Project.class);
	}

	@Test
	public void test1_SaveOrUpdate() {
		Project savedProject = this.projectRepository.save(this.project);
		assertThat(this.project.getName().equalsIgnoreCase(savedProject.getName()));
	}

	@Test
	public void test2_FindById() {
		Project savedProject = this.projectRepository.save(this.project);
		Project project = this.projectRepository.findById(savedProject.getProjectId()).get();
		assertThat(project.getName().equalsIgnoreCase(this.project.getName()));
	}
}
