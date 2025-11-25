package com.me.service;

import com.me.transport.controller.model.PersonRq;
import com.me.transport.controller.model.PersonRs;

public interface PersonService {

    PersonRs create(PersonRq rq);

    PersonRs getCurrent();

}
