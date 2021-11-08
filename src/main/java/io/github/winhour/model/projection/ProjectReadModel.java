package io.github.winhour.model.projection;

import io.github.winhour.model.Project;

public class ProjectReadModel {

    private String description;

    public ProjectReadModel(Project source) {
        this.description = source.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
