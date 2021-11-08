package io.github.winhour.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import io.github.winhour.model.TaskGroup;

@Entity
//@Inheritance(InheritanceType.TABLE_PER_CLASS)
@Table(name = "tasks")
public class Task extends BaseTask{

    private LocalDateTime deadline;

    @Embedded
    /*@AttributeOverrides({
            @AttributeOverride(column = @Column(name = "updatedOn"), name = "updatedOn")
    })*/
    private Audit audit = new Audit();

    @ManyToOne//(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    public Task(){
    }

    public Task(String description, LocalDateTime deadline){

        this.setDescription(description);
        this.deadline = deadline;

    }

    public Task(String description, LocalDateTime deadline, TaskGroup group){

        this(description, deadline);
        this.group = group;

    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    TaskGroup getGroup() {
        return group;
    }

    void setGroup(TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source){
        this.setDescription(source.getDescription());
        this.setDone(source.isDone());
        deadline = source.deadline;
        group = source.group;
    }



}
