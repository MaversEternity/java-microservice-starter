package com.me.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.me.db.entity.PersonEntity;

public interface PersonRepo extends JpaRepository<PersonEntity, Long> {

}
