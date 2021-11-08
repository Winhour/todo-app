package io.github.winhour.controller;

import io.github.winhour.model.Task;
import io.github.winhour.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TaskController.class)
@ActiveProfiles("integration")
public class TaskControllerLightIntegrationTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask(){

        //given

        String description = "foo";

        when(repo.findById(anyInt()))
                .thenReturn(Optional.of(new Task(description, LocalDateTime.now())));
        //int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        //when + then

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/tasks/123"))
                    .andDo(print())
                    //.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
                    .andExpect(content().string(containsString(description)));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    void httpGet_returnsListOfAllTasks(){

        //given

        Task task1 = new Task ("task 1", LocalDateTime.now());
        Task task2 = new Task("task 2", LocalDateTime.now());

        List<Task> taskList = new ArrayList<>();
        taskList.add(task1);
        taskList.add(task2);

        when(repo.findAll()).thenReturn(taskList);

        //when + then

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
