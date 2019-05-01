package com.project.manager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.manager.model.Project;
import com.project.manager.service.ProjectManagerService;

@RestController
@RequestMapping("/projectservice/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjectController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
	@Autowired
	private ProjectManagerService projectManagerService;

	@GetMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Project>> getAllProjects() {
		return new ResponseEntity<List<Project>>(this.projectManagerService.getProjectList(), HttpStatus.OK);
	}

	@PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Project> saveOrUpdate(@RequestBody Project project) {
		return new ResponseEntity<Project>(this.projectManagerService.saveOrUpdateProject(project), HttpStatus.OK);
	}
}
