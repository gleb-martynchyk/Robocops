package org.jazzteam.martynchyk.service.implementation;

import com.mongodb.client.result.UpdateResult;
import org.jazzteam.martynchyk.dao.implementation.TaskDao;
import org.jazzteam.martynchyk.service.BaseService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Service
public class TaskService implements BaseService<BaseTask> {

    @Autowired
    private TaskDao taskDao;

    @Override
    public BaseTask create(BaseTask object) {
        return taskDao.create(object);
    }

    @Override
    public BaseTask findById(long id) {
        return taskDao.findById(id);
    }

    public BaseTask findNext() {
        return taskDao.findNext();
    }

    public boolean hasNextToExecute() {
        return taskDao.find(eq("status", "CREATED")).iterator().hasNext();
    }

    @Override
    public Iterable<BaseTask> findAll() {
        return taskDao.findAll();
    }

    @Override
    public UpdateResult update(BaseTask object) {
        return taskDao.update(object);
    }

    @Override
    public void deleteById(long id) {
        taskDao.deleteById(id);
    }

    public void deleteMany(List<Long> ids) {
        taskDao.deleteMany(ids);
    }
}
