package io.github.winhour.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
public class BaseProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "description")
    @NotBlank(message = "Description must be not null")
    private String description;

    public BaseProject(){
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }


}
