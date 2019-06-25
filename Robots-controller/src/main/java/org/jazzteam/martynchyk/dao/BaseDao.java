package org.jazzteam.martynchyk.dao;

import java.util.List;

public interface BaseDao<T> {
    T create(T object);

    T findById(long id);

    List<T> findAll();

    T update(T object);

    void deleteById(long id);
}
