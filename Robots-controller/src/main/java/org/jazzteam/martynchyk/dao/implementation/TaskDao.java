package org.jazzteam.martynchyk.dao.implementation;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.jazzteam.martynchyk.config.TaskPriorityCodecProvider;
import org.jazzteam.martynchyk.dao.BaseDao;
import org.jazzteam.martynchyk.tasks.BaseTask;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Sorts.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class TaskDao implements BaseDao<BaseTask> {

    public BaseTask create(BaseTask task) {
        getCollection().insertOne(task);
        return task;
    }

    public BaseTask findPriority() {

        //TODO так же можно написать через аггрегацию, но не получилось
//        AggregateIterable<BaseTask> aggregate = getCollection().aggregate(
//                Arrays.asList(
////                        Aggregates.match(Filters.eq("status", "CREATED"))
//                        Aggregates.group("$_id", Accumulators.max("minQuantity", "$createDate"))
//                )
//        );
//        return aggregate.first();

        return getCollection()
                .find(eq("status", "CREATED"))
                .sort(orderBy(
                        descending("taskPriority.priority"),
                        ascending("createDate")))
                .limit(1).first();
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

    private MongoCollection<BaseTask> getCollection() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider.builder().automatic(true).build(),
                        new TaskPriorityCodecProvider()
                ));

        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .build();
        MongoClient client = MongoClients.create(settings);

        MongoDatabase database = client.getDatabase("Robocops");
        return database.getCollection("tasks", BaseTask.class);
    }


}
