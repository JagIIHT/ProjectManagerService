package com.project.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.manager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findOneUserByProjectId(String projectId);
}
