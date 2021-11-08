package io.github.winhour.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer i);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    Task save(Task entity);

    /*@RestResource(path="done", rel = "done")*/
    List<Task> findByDone(@Param("state") boolean done);

    List<Task> findAllByGroup_Id(Integer groupId);

    List<Task> findByDoneIsFalseAndDeadlineIsNullOrDeadlineIsLessThanEqual(LocalDateTime date);
}
