package io.github.winhour.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup entity);

    List<TaskGroup> findAllByDoneIsFalseAndProject_Id(Integer projectId);

    /*TaskRepository getTasks();*/

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

    boolean existsById(Integer id);

}
