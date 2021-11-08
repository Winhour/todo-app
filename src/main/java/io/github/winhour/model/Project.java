package io.github.winhour.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project extends BaseProject{

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project"/*, fetch = FetchType.EAGER*/)
    public Set<ProjectStep> steps;                  //fix later

    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;

    public Project() {
    }

    public Project(String description) {
        this.setDescription(description);
    }


    public Set<ProjectStep> getProjectsteps() {
        return steps;
    }

    public void setProjectsteps(Set<ProjectStep> steps) {
        this.steps = steps;
    }

    public Set<TaskGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<TaskGroup> groups) {
        this.groups = groups;
    }
}
