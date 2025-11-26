package com.me.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.me.db.entity.TaskEntity;
import com.me.transport.http.model.TaskRq;
import com.me.transport.http.model.TaskRs;

@Mapper(componentModel = "spring", uses = {JpaRefMapper.class, PersonMapper.class})
public interface TaskMapper {

    @Mapping(target = "assignedBy", source = "assignee")
    @Mapping(target = "lastCreatedBy", source = "creator")
    @Mapping(target = "lastUpdatedBy", source = "updater")
    TaskRs to(TaskEntity task);

    List<TaskRs> to(Stream<TaskEntity> task);

    @Mapping(target = "assignee", source = "assignedBy")
    TaskEntity to(TaskRq rq);

    TaskEntity update(@MappingTarget TaskEntity entity, TaskRq task);

}
