package io.github.winhour.logic;

import io.github.winhour.model.*;
import io.github.winhour.model.projection.GroupReadModel;
import io.github.winhour.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

//@Service
//@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
//@RequestScope
public class TaskGroupService {

    /*
    @Autowired
    List<String> temp(TaskGroupRepository repository){
        // FIXME: N + 1
        return repository.findAll()
                .stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                .map(Task::getDescription)
                .collect(Collectors.toList());
    }
    */

    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source){
        return createGroup(source, null);
    }

    GroupReadModel createGroup(GroupWriteModel source, final Project project){
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId){

        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks. Do all the tasks first.");
        }

        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));

        result.setDone(!result.isDone());
        repository.save(result);

    }


}
