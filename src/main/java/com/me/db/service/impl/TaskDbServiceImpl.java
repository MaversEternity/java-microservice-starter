package com.me.db.service.impl;

import org.springframework.stereotype.Service;

import com.me.db.entity.TaskEntity;
import com.me.db.repository.TaskRepo;
import com.me.db.service.TaskDbService;

@Service
public class TaskDbServiceImpl extends BaseDbService<TaskEntity, Long> implements TaskDbService {

    public TaskDbServiceImpl(TaskRepo repository) {
        super(repository);
    }
}
