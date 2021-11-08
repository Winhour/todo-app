package io.github.winhour.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

public class TestTaskRepository implements TaskRepository{

    private Map<Integer, Task> tasks = new HashMap<>();

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Page<Task> findAll(Pageable page) {
        return null;
    }

    @Override
    public Optional<Task> findById(Integer i) {
        return Optional.ofNullable(tasks.get(i));
    }

    @Override
    public boolean existsById(Integer id) {
        return tasks.containsKey(id);
    }

    @Override
    public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
        return false;
    }

    @Override
    public Task save(Task entity) {

        int key = tasks.size() + 1;

        try {
            var field = BaseTask.class.getDeclaredField("id");;
            field.setAccessible(true);
            field.set(entity, key);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        tasks.put(key, entity);

        return tasks.get(key);

    }

    @Override
    public List<Task> findByDone(boolean done) {
        return null;
    }

    @Override
    public List<Task> findAllByGroup_Id(Integer groupId) {
        return List.of();
    }

    @Override
    public List<Task> findByDoneIsFalseAndDeadlineIsNullOrDeadlineIsLessThanEqual(LocalDateTime date) {
        return List.of();
    }
}
