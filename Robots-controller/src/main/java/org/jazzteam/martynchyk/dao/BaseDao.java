package org.jazzteam.martynchyk.dao;

import com.mongodb.client.result.UpdateResult;

public interface BaseDao<T> {
    T create(T object);

//    T findNext(Bson filter);

    T findById(long id);

    Iterable<T> findAll();

    UpdateResult update(T object);

    void deleteById(long id);
}
