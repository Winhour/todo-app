package io.github.winhour.controller;

import io.github.winhour.logic.ProjectService;
import io.github.winhour.model.Project;
import io.github.winhour.model.ProjectStep;
import io.github.winhour.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    String showProjects(Model model){
        var projectToEdit = new ProjectWriteModel();
        //projectToEdit.setDescription("test");
        model.addAttribute("project", projectToEdit);
        return "projects";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectWriteModel current,
            BindingResult bindingResult,
            Model model
    ){
        if(bindingResult.hasErrors()){
            return "projects";
        }
        projectService.save(current.toProject());
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Dodano projekt!");
        return "projects";
    }


    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
        return projectService.readAll();
    }





}
