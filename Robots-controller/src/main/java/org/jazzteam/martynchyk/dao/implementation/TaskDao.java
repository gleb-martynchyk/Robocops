package org.jazzteam.martynchyk.dao.implementation;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jazzteam.martynchyk.dao.BaseDao;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Sorts.*;

@Repository
public class TaskDao implements BaseDao<BaseTask> {

    @Autowired
    private MongoClient client;

    private MongoCollection<BaseTask> getCollection() {
        MongoDatabase database = client.getDatabase("Robocops");
        return database.getCollection("tasks", BaseTask.class);
    }

    public BaseTask create(BaseTask task) {
        getCollection().insertOne(task);
        return task;
    }

    public BaseTask findNext() {
        return getCollection()
                .find(eq("status", "CREATED"))
                .sort(orderBy(
                        descending("taskPriority.priority"),
                        ascending("createDate")))
                .limit(1).first();
    }

    public Iterable<BaseTask> find(Bson filter) {
        return getCollection().find(filter);
    }

    public BaseTask findById(long id) {
        return getCollection().find(eq("_id", id)).first();
    }

    public Iterable<BaseTask> findAll() {
        return getCollection().find();
    }

    public UpdateResult update(BaseTask task) {
        Bson document = new Document("$set", task);
        return getCollection().updateOne(eq("_id", task.getId()), document);
    }

    public void deleteById(long id) {
        getCollection().deleteOne(eq("_id", id));
    }

    public void deleteMany(List<Long> ids) {
        getCollection().deleteMany(in("_id", ids));
    }
}
