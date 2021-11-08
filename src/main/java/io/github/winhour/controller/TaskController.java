package io.github.winhour.controller;

import io.github.winhour.logic.TaskService;
import io.github.winhour.model.Task;
import io.github.winhour.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;

    TaskController(/*@Qualifier("SQLTaskRepository")*/ final TaskRepository repository, TaskService service){
        this.repository = repository;
        this.service = service;
    }

    //@RequestMapping(method = RequestMethod.GET, path = "/tasks")
    @GetMapping(params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
        /*logger.warn("Exposing all the tasks!");
        return service.findAllAsync().thenApply(ResponseEntity::ok);*/

    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.warn("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    /*@GetMapping("/tasks/{id}")
    ResponseEntity<?> readTask(@PathVariable int id){
        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repository.findById(id));
    }*/


    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id)
    {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*@GetMapping(value = "/search/done", produces = MediaType.APPLICATION_JSON_VALUE)
    String foo(){return "";}

    @GetMapping(value = "/search/done", produces = MediaType.TEXT_XML_VALUE)
    String bar(){return "";}*/

    @GetMapping("/search/done")
    ResponseEntity<List<Task>>  readDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

    @PostMapping
    ResponseEntity<?> postTask(@RequestBody @Valid Task toPost){

        repository.save(toPost);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {

        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom((toUpdate));
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();

    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {

        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();

    }

    /*
    @Transactional
    public void foobar(){
        this.toggleTask(1);
    }
`   */

}
