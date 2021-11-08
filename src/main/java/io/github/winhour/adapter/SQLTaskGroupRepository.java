package io.github.winhour.adapter;

import io.github.winhour.model.TaskGroup;
import io.github.winhour.model.TaskGroupRepository;
import io.github.winhour.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SQLTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    @Override
    @Query("select distinct g from TaskGroup g join fetch g.tasks")
    List<TaskGroup> findAll();

    @Override
    List<TaskGroup> findAllByDoneIsFalseAndProject_Id(Integer projectId);

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

    @Override
    boolean existsById(Integer id);

   /* @Override
    TaskRepository getTasks();*/
}
