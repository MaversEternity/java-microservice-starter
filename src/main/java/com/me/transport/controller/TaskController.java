
package com.me.transport.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me.service.TaskService;
import com.me.transport.controller.model.TaskRq;
import com.me.transport.controller.model.TaskRs;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskRs> get() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public TaskRs get(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    public TaskRs create(@Valid @RequestBody TaskRq rq) {
        return taskService.create(rq);
    }

    @PutMapping("/{id}")
    public TaskRs update(@PathVariable Long id, @Valid @RequestBody TaskRq rq) {
        return taskService.update(id, rq);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        taskService.delete(id);
        return true;
    }

}
