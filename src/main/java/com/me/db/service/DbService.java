package com.me.db.service;

import java.util.List;
import java.util.Optional;

import com.me.exception.ErrorCode;

/**
 * Db layer service that includes closer to db aggregation logic and basic method skeleton
 *
 * @param <E> entity type
 * @param <I> id type
 */
public interface DbService<E, I> extends TransactionService {

    /**
     * Gets an entity by id otherwise throws exception
     *
     * @param id entity id
     * @throws com.me.exception.BusinessException with error code {@link ErrorCode#NOT_FOUND}
     * @return entity
     */
    E getById(I id);

    /**
     * Finds an entity by id wrapped with Optional
     *
     * @param id entity id
     * @return optional of entity
     */
    Optional<E> findById(I id);

    /**
     * Finds all entities
     *
     * @return list of entities
     */
    List<E> findAll();

    /**
     * Finds all entities by ids
     *
     * @param ids entity ids
     * @return list of entities
     */
    List<E> findAllById(Iterable<I> ids);

    /**
     * Saves entity
     *
     * @param entity entity
     * @return saved entity
     */
    E save(E entity);

    /**
     * Deletes an entity by id otherwise throws exception
     *
     * @param id entity id
     */
    void deleteById(I id);

}
