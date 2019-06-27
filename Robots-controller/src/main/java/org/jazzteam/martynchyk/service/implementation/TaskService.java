package org.jazzteam.martynchyk.service.implementation;

import com.mongodb.client.result.UpdateResult;
import org.jazzteam.martynchyk.dao.implementation.TaskDao;
import org.jazzteam.martynchyk.service.BaseService;
import org.jazzteam.martynchyk.tasks.BaseTask;

import java.util.List;

public class TaskService implements BaseService<BaseTask> {

    private TaskDao taskDao = new TaskDao();

    @Override
    public BaseTask create(BaseTask object) {
        return taskDao.create(object);
    }

    @Override
    public BaseTask findById(long id) {
        return taskDao.findById(id);
    }

    public BaseTask findPriority() {
        return taskDao.findPriority();
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

    public void deleteById(List<Long> ids) {
        taskDao.deleteMany(ids);
    }
}
