package com.me.db.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.me.db.entity.PersonEntity;
import com.me.db.service.PersonDbService;
import com.me.db.service.SystemUserProvider;
import com.me.exception.BusinessException;

import static com.me.common.CacheConstants.SYSTEM_USER_ID_CACHE;

@Service
public class PersonDbServiceImpl extends BaseDbService<PersonEntity, Long> implements PersonDbService, SystemUserProvider {

    private static final Long SYSTEM_USER_ID = 0L;

    public PersonDbServiceImpl(JpaRepository<PersonEntity, Long> repository) {
        super(repository);
    }

    @Cacheable(SYSTEM_USER_ID_CACHE)
    @Override
    public Long getSystemId() {
        return repository.findById(SYSTEM_USER_ID)
            .map(PersonEntity::getId)
            .orElseThrow(() -> BusinessException.notFound(PersonEntity.class, SYSTEM_USER_ID));
    }
}
