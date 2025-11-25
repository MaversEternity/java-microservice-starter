package com.me.db.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.me.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseDbService<E, I> {

    protected final JpaRepository<E, I> repository;

    @Transactional(readOnly = true)
    public E getById(I id) {
        return repository.findById(id)
            .orElseThrow(() -> BusinessException.notFound(getEntityClass(), id));
    }

    @Transactional(readOnly = true)
    public Optional<E> findById(I id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<E> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<E> findAllById(Iterable<I> ids) {
        return repository.findAllById(ids);
    }

    @Transactional
    public E save(E entity) {
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(I id) {
        repository.deleteById(id);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public <T> T execute(Supplier<T> task) {
        return task.get();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void execute(Runnable task) {
        task.run();
    }

    @SuppressWarnings("unchecked")
    private Class<E> getEntityClass() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<E>) type.getActualTypeArguments()[0];
    }
}
