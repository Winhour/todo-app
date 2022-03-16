package io.github.winhour.controller;

import io.github.winhour.logic.TaskGroupService;
import io.github.winhour.model.Task;
import io.github.winhour.model.TaskGroupRepository;
import io.github.winhour.model.TaskRepository;
import io.github.winhour.model.projection.GroupReadModel;
import io.github.winhour.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/* Controller for Task Groups */

@RestController
@RequestMapping("/groups")
public class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupRepository tgRepository;
    private final TaskGroupService service;
    private final TaskRepository repository;

    public TaskGroupController(TaskGroupRepository tgRepository, TaskGroupService service, TaskRepository repository) {
        this.tgRepository = tgRepository;
        this.service = service;
        this.repository = repository;
    }

    /*@GetMapping
    ResponseEntity<List<TaskGroup>> readAllGroups(){
        logger.warn("Reading all groups");
        return ResponseEntity.ok(tgRepository.findAll());
    }

    @PostMapping
    ResponseEntity<?> postGroup(@RequestBody @Valid TaskGroup toPost){

        tgRepository.save(toPost);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {

        if (!tgRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        tgRepository.findById(id)
                .ifPresent(taskGroup -> taskGroup.setDone(!taskGroup.isDone()));
        return ResponseEntity.noContent().build();

    }*/

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String displayGroups(Model model){
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }


    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> postGroup(@RequestBody @Valid GroupWriteModel toPost){
        GroupReadModel result = service.createGroup(toPost);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);

    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
        return ResponseEntity.ok(service.readAll());
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @ResponseBody
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {

        service.toggleGroup(id);
        return ResponseEntity.noContent().build();

    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/search/undone")
    ResponseEntity<List<Task>> readUndoneTask() {
        return ResponseEntity.ok(repository.findByDoneIsFalseAndDeadlineIsNullOrDeadlineIsLessThanEqual(LocalDate.now().atTime(LocalTime.MAX)));
    }


    /*@GetMapping("/{id}/tasks")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
        logger.warn("Reading all task from group");

        repository = tgrepository.getTasks();

        return ResponseEntity.ok(repository.findAll());
    }*/

}
