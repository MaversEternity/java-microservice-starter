package com.me.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me.db.entity.TaskEntity;
import com.me.db.service.TaskDbService;
import com.me.mapper.TaskMapper;
import com.me.service.TaskService;
import com.me.transport.controller.model.TaskRq;
import com.me.transport.controller.model.TaskRs;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskDbService taskDbService;
    private final TaskMapper taskMapper;

    @Override
    public TaskRs create(TaskRq rq) {
        TaskEntity saved = taskDbService.save(taskMapper.to(rq));
        return taskMapper.to(saved);
    }

    @Transactional
    @Override
    public TaskRs update(Long id, TaskRq rq) {
        TaskEntity task = taskDbService.getById(id);
        taskMapper.update(task, rq);
        TaskEntity saved = taskDbService.save(task);
        return taskMapper.to(saved);
    }

    @Override
    public List<TaskRs> findAll() {
        return taskMapper.to(taskDbService.findAll().stream());
    }

    @Override
    public TaskRs getById(Long id) {
        return taskMapper.to(taskDbService.getById(id));
    }

    @Override
    public void delete(Long id) {
        taskDbService.deleteById(id);
    }
}
