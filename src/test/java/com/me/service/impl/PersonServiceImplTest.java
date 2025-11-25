package com.me.service.impl;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.me.SecurityMock;
import com.me.db.entity.PersonEntity;
import com.me.db.service.PersonDbService;
import com.me.mapper.PersonMapper;
import com.me.transport.controller.model.PersonRq;
import com.me.transport.controller.model.PersonRs;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest implements SecurityMock {

    @Mock
    private PersonDbService personDbService;
    @Mock
    private PersonMapper personMapper;
    @InjectMocks
    private PersonServiceImpl service;

    @Test
    void create_happyPath_success() {
        PersonRq rq = Instancio.create(PersonRq.class);
        PersonEntity person = Instancio.create(PersonEntity.class);
        PersonRs rs = Instancio.create(PersonRs.class);

        when(personMapper.to(any(PersonRq.class))).thenReturn(person);
        when(personDbService.save(any())).thenReturn(person);
        when(personMapper.to(any(PersonEntity.class))).thenReturn(rs);

        PersonRs result = service.create(rq);

        assertNotNull(result);
        verify(personDbService).save(person);
    }

    @Test
    void getCurrent() {
        PersonEntity person = Instancio.create(PersonEntity.class);
        PersonRs rs = Instancio.create(PersonRs.class);

        when(personDbService.getById(any())).thenReturn(person);
        when(personMapper.to(any(PersonEntity.class))).thenReturn(rs);

        PersonRs result = service.getCurrent();

        assertNotNull(result);
        verify(personDbService).getById(SecurityMock.USER_ID);
    }
}