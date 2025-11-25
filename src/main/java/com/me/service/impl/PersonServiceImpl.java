package com.me.service.impl;

import org.springframework.stereotype.Service;

import com.me.db.entity.PersonEntity;
import com.me.db.service.PersonDbService;
import com.me.mapper.PersonMapper;
import com.me.service.PersonService;
import com.me.transport.controller.model.PersonRq;
import com.me.transport.controller.model.PersonRs;

import lombok.RequiredArgsConstructor;

import static com.me.util.SecurityUtils.getAuthContext;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonDbService personDbService;
    private final PersonMapper personMapper;

    @Override
    public PersonRs create(PersonRq rq) {
        PersonEntity saved = personDbService.save(personMapper.to(rq));
        return personMapper.to(saved);
    }

    @Override
    public PersonRs getCurrent() {
        PersonEntity person = personDbService.getById(getAuthContext().localId());
        return personMapper.to(person);
    }

}
