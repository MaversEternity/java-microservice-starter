package com.me.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

import com.me.db.entity.IdEntity;

import jakarta.persistence.EntityManager;

@Mapper(componentModel = "spring")
public abstract class JpaRefMapper {

    @Autowired
    private EntityManager entityManager;

    public <T extends IdEntity<Long>> List<T> createProxies(Collection<Long> references, @TargetType Class<T> entityClass) {
        return references.stream().filter(Objects::nonNull).map(id -> createProxy(id, entityClass)).toList();
    }

    public <T extends IdEntity<Long>> T createProxy(Long reference, @TargetType Class<T> entityClass) {
        return reference != null ? entityManager.getReference(entityClass, reference) : null;
    }

    public Long toId(IdEntity<Long> entity) {
        return entity != null ? entity.getId() : null;
    }

}
