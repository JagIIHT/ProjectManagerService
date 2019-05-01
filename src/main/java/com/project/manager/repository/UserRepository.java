package com.project.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.manager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
