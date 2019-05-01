package com.project.manager.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.project.manager.model.Parent;
import com.project.manager.model.Project;
import com.project.manager.model.Task;
import com.project.manager.repository.ParentTaskRepository;
import com.project.manager.repository.ProjectRepository;
import com.project.manager.repository.TaskRepository;
import com.project.manager.repository.UserRepository;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ParentTaskRepository ParentTaskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Task> getAllTasks() {
		return (List<Task>) this.taskRepository.findAll();
	}

	@Override
	public Task saveTask(Task task) {
		return this.taskRepository.save(task);
	}

	@Override
	public Task getTaskById(String id) {
		Optional<Task> opTask = this.taskRepository.findById(Integer.parseInt(id));
		if (!opTask.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return opTask.get();
	}

	@Override
	public Task endTask(String taskId) {
		Task task = getTaskById(taskId);
		LocalDate currentDate = LocalDate.now();
		task.setEndDate(currentDate);
		Optional<Parent> parent = this.ParentTaskRepository.findById(task.getId());
		if (parent.isPresent()) {
			parent.get().getTasks().forEach(t -> {
				t.setEndDate(currentDate);
				this.taskRepository.save(t);
			});
		}
		return this.taskRepository.save(task);
	}

	@Override
	public Project saveOrUpdateProject(Project project) {
		return this.projectRepository.save(project);
	}

	@Override
	public List<Project> getProjectList() {
		return this.projectRepository.findAll();
	}

}
