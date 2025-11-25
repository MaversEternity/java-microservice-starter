package com.me.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.me.db.entity.TaskEntity;

public interface TaskRepo extends JpaRepository<TaskEntity, Long> {

}
