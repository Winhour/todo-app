package io.github.winhour.logic;
import io.github.winhour.TaskConfigurationProperties;
import io.github.winhour.model.*;
import io.github.winhour.model.projection.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service
public class ProjectService {

    private ProjectRepository repository;
    private TaskConfigurationProperties config;
    private TaskGroupRepository tgRepository;
    private TaskGroupService service;

    //@Autowired
    public ProjectService(ProjectRepository repository, TaskConfigurationProperties config, TaskGroupRepository tgRepository, TaskGroupService service) {
        this.repository = repository;
        this.config = config;
        this.tgRepository = tgRepository;
        this.service = service;
    }

    public List<ProjectReadModel> readAllPrm(){
        return repository.findAll().stream()
                .map(ProjectReadModel::new)
                .collect(Collectors.toList());
    }

    public List<Project> readAll(){
        return repository.findAll();
    }

    public void saveProject(ProjectWriteModel pwm){
        repository.save(pwm.toProject());
    }

    public Project save(final Project toSave){
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline){

        if (!config.getTemplate().isAllowMultipleTasks() && tgRepository.existsByDoneIsFalseAndProject_Id(projectId)){
            throw new IllegalStateException("Only one undone group from project is allowed");
        }

        TaskGroup result = new TaskGroup();
        result.setProject(repository.findById(projectId).orElse(null));

        if (result.getProject() == null){
            throw new IllegalArgumentException("There is no project with the given projectId");
        }

        var targetGroup = new GroupWriteModel();

        targetGroup.setDescription(result.getProject().getDescription());

        result.setDescription(result.getProject().getDescription());
        result.setTasks(result.getProject().getProjectsteps().stream()
                .map(step ->
                    new Task(step.getDescription(), deadline.plusDays(step.getDays_to_deadline())))
                .collect(Collectors.toSet()));

        targetGroup.setTasks(result.getProject().getProjectsteps().stream()
                .map(projectStep -> {

                    var task = new GroupTaskWriteModel();
                    task.setDescription(projectStep.getDescription());
                    task.setDeadline(deadline.plusDays(projectStep.getDays_to_deadline()));
                    return task;
                }).collect(Collectors.toSet())
        );

        service.createGroup(targetGroup, result.getProject());

        tgRepository.save(result);

        GroupReadModel actualResult = new GroupReadModel(result);

        //actualResult.setDeadline(deadline);

        return actualResult;
    }


}
