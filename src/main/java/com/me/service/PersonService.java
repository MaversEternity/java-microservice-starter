package com.me.service;

import com.me.transport.http.model.PersonRq;
import com.me.transport.http.model.PersonRs;

public interface PersonService {

    PersonRs create(PersonRq rq);

    PersonRs getCurrent();

}
