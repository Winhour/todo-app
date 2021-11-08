package io.github.winhour.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
//@Inheritance(InheritanceType.TABLE_PER_CLASS)
@Table(name = "task_groups")
public class TaskGroup extends BaseTask{

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group"/*,fetch = FetchType.LAZY*/)
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Embedded
    private Audit audit = new Audit();


    public TaskGroup(){
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    /*public void updateFrom(final TaskGroup source){
        description = source.description;
        done = source.done;
    }*/

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Audit getAudit() {
        return audit;
    }

    void setAudit(Audit audit) {
        this.audit = audit;
    }
}
