package com.project.manager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.project.manager.model.Task;
import com.project.manager.service.TaskService;

@RestController
@RequestMapping("/taskservice/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	@Autowired
	private TaskService taskService;

	@GetMapping(value = "tasks", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllTasks(Model model) {
		logger.info("<-- Inside getAllTasks -->");
		List<Task> tasks = this.taskService.getAllTasks();
		if (tasks == null || tasks.isEmpty()) {
			return new ResponseEntity<Object>(tasks, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Object>(tasks, HttpStatus.OK);
	}

	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> updateTask(@RequestBody Task task) {
		return new ResponseEntity<Task>(this.taskService.saveTask(task), HttpStatus.OK);
	}

	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> addTask(@RequestBody Task task) {
		return new ResponseEntity<Task>(this.taskService.saveTask(task), HttpStatus.OK);
	}

	@PutMapping(value = "/endtask/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> updateEndDate(@PathVariable(name = "id") String taskId) {
		return new ResponseEntity<Task>(this.taskService.endTask(taskId), HttpStatus.OK);
	}

	@GetMapping(value = "/task/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> getTask(@PathVariable(name = "id") String taskId) {
		return new ResponseEntity<Task>(this.taskService.getTaskById(taskId), HttpStatus.OK);
	}

	@ExceptionHandler(value = { HttpMessageNotReadableException.class, MethodArgumentNotValidException.class })
	public ResponseEntity<Object> exceptionHandler(Exception ex, WebRequest req) {
		logger.error(ex.getMessage());
		return new ResponseEntity<>("Request data invalid", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { EmptyResultDataAccessException.class })
	public ResponseEntity<Object> emptyResultExceptionHandler(Exception ex, WebRequest req) {
		logger.error(ex.getMessage());
		return new ResponseEntity<>("Record not found", HttpStatus.BAD_REQUEST);
	}
}
