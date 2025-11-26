package com.me.service;

import java.util.List;

import com.me.transport.http.model.TaskRq;
import com.me.transport.http.model.TaskRs;

public interface TaskService {

    TaskRs create(TaskRq rq);

    TaskRs update(Long id, TaskRq rq);

    List<TaskRs> findAll();

    TaskRs getById(Long id);

    void delete(Long id);

}
