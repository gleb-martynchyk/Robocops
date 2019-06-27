package org.jazzteam.martynchyk.service;

import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;

public interface BaseService<T> {
    T create(T object);

    T findById(long id);

    Iterable<T> findAll();

    UpdateResult update(T object);

    void deleteById(long id);
}
