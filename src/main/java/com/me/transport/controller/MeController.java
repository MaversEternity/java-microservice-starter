package com.me.transport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me.service.PersonService;
import com.me.transport.controller.model.PersonRq;
import com.me.transport.controller.model.PersonRs;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final PersonService personService;

    @GetMapping
    public PersonRs get() {
        return personService.getCurrent();
    }

    @PostMapping
    public PersonRs create(@Valid @RequestBody PersonRq rq) {
        return personService.create(rq);
    }

}
