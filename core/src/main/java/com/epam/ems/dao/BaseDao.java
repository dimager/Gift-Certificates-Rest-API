package com.epam.ems.dao;

import com.epam.ems.entity.BaseEntity;

import java.util.List;

/**
 * @param <T> class that must extend BaseDao.
 *            Interface {@link BaseDao} helps to implement CRUD methods to {@link T}.
 */
public interface BaseDao<T extends BaseEntity> {

    /**
     * Allows deleting {@link T} entity from DB by id.
     *
     * @param id {@link T} id.
     * @return true - if object is deleted, otherwise - false.
     */
    boolean delete(long id);

    /**
     * Allows getting list of all {@link T} from DB.
     * @param offset offset
     * @param size page size
     * @return List of {@link T}.
     */
    List<T> getAll(int size, int offset);

    /**
     * Allows getting {@link T} by id.
     *
     * @param id {@link T} id
     * @return {@link T} object by id
     */
    T getById(long id);

    /**
     * Allows updating {@link T} entity in DB.
     *
     * @param t {@link T} new data.
     * @return updated object.
     */
    T update(T t);

    /**
     * Allows creating {@link T} entity in DB.
     *
     * @param t {@link T} new data.
     * @return created {@link T} with generated ID.
     */
    T create(T t);

}
