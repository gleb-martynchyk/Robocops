package org.jazzteam.martynchyk.dao;

import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;

public interface BaseDao<T> {
    T create(T object);

//    T findNext(Bson filter);

    T findById(long id);

    Iterable<T> findAll();

    UpdateResult update(T object);

    void deleteById(long id);
}
