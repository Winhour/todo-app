package io.github.winhour.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "project_steps")
public class ProjectStep extends BaseProject{

    private int daysToDeadline;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public ProjectStep(){
    }

    public int getDays_to_deadline() {
        return daysToDeadline;
    }

    public void setDays_to_deadline(int daysToDeadline) {
        this.daysToDeadline = daysToDeadline;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
