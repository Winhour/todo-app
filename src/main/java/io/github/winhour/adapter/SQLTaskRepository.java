package io.github.winhour.adapter;

import io.github.winhour.model.Task;
import io.github.winhour.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository                                                      //(path = "todos", collectionResourceRel = "todos")
public interface SQLTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    /*@Override
    @RestResource(exported = false)
    void deleteById(Integer integer);

    @Override
    @RestResource(exported = false)
    void delete(Task task);

    @RestResource(path="done", rel = "done")
    List<Task> findByDone(@Param("state") boolean done);*/

    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id = :id")
    boolean existsById(Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    @Override
    List<Task> findAllByGroup_Id(Integer groupId);

    @Override
    List<Task> findByDoneIsFalseAndDeadlineIsNullOrDeadlineIsLessThanEqual(LocalDateTime date);
}
