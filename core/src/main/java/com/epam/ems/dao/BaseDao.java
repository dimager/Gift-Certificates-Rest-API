package com.epam.ems.dao;

import com.epam.ems.entity.BaseEntity;

import java.util.List;

public interface BaseDao<T extends BaseEntity> {
    boolean delete(long id);
    List<T> getAll();
    T getById(long id);
    T update(T t);
    T create(T t);
}
