package com.me.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.me.db.entity.PersonEntity;
import com.me.transport.controller.model.PersonRq;
import com.me.transport.controller.model.PersonRs;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonRs to(PersonEntity task);

    List<PersonRs> to(Stream<PersonEntity> task);

    PersonEntity to(PersonRq rq);

    PersonEntity update(@MappingTarget PersonEntity entity, PersonRq task);

}
