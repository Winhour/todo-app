package io.github.winhour.model.projection;

import io.github.winhour.model.Project;
import io.github.winhour.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {

    @NotBlank(message = "Project description must not be empty")
    private String description;

    @Valid
    private List<ProjectStep> steps = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public ProjectWriteModel(){
        steps.add(new ProjectStep());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject(){

        var result = new Project();

        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setProjectsteps(new HashSet<>(steps));

        return result;
    }




}
