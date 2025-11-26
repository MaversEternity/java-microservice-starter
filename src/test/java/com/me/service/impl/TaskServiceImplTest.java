package com.me.service.impl;

import java.util.List;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.me.db.entity.TaskEntity;
import com.me.db.service.TaskDbService;
import com.me.mapper.TaskMapper;
import com.me.transport.http.model.TaskRq;
import com.me.transport.http.model.TaskRs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskDbService taskDbService;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void create_happyPath_success() {
        TaskRq rq = Instancio.create(TaskRq.class);
        TaskEntity task = Instancio.create(TaskEntity.class);
        TaskRs response = Instancio.create(TaskRs.class);

        when(taskMapper.to(any(TaskRq.class))).thenReturn(task);
        when(taskDbService.save(any(TaskEntity.class))).thenReturn(task);
        when(taskMapper.to(any(TaskEntity.class))).thenReturn(response);

        TaskRs result = taskService.create(rq);

        assertNotNull(result);
        verify(taskDbService).save(task);
    }

    @Test
    void update_happyPath_success() {
        TaskRq rq = Instancio.create(TaskRq.class);
        TaskEntity task = Instancio.create(TaskEntity.class);
        TaskRs response = Instancio.create(TaskRs.class);

        when(taskDbService.getById(any())).thenReturn(task);
        when(taskMapper.update(any(), any(TaskRq.class))).thenReturn(task);
        when(taskDbService.save(any(TaskEntity.class))).thenReturn(task);
        when(taskMapper.to(any(TaskEntity.class))).thenReturn(response);

        TaskRs result = taskService.update(1L, rq);

        assertNotNull(result);
        verify(taskDbService).getById(1L);
        verify(taskDbService).save(task);
    }

    @Test
    void findAll_happyPath_success() {
        List<TaskEntity> entities = Instancio.ofList(TaskEntity.class).create();
        List<TaskRs> responses = Instancio.ofList(TaskRs.class).size(entities.size()).create();

        when(taskDbService.findAll()).thenReturn(entities);
        when(taskMapper.to(ArgumentMatchers.<Stream<TaskEntity>>any())).thenReturn(responses);

        List<TaskRs> result = taskService.findAll();

        assertEquals(responses.size(), result.size());
        verify(taskDbService).findAll();
    }

    @Test
    void getById_happyPath_success() {
        TaskEntity task = Instancio.create(TaskEntity.class);
        TaskRs response = Instancio.create(TaskRs.class);

        when(taskDbService.getById(any())).thenReturn(task);
        when(taskMapper.to(any(TaskEntity.class))).thenReturn(response);

        TaskRs result = taskService.getById(1L);

        assertNotNull(result);
        verify(taskDbService).getById(1L);
    }

    @Test
    void delete_happyPath_success() {
        taskService.delete(1L);

        verify(taskDbService).deleteById(1L);
    }
}