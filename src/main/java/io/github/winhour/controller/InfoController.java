package io.github.winhour.controller;

import io.github.winhour.TaskConfigurationProperties;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/info")
public class InfoController {

    /*@Value("${spring.datasource.url}")
    private String url;*/

    /*@Autowired*/
    private DataSourceProperties dataSource;

    /*@Value("${task.allowMultipleTasksFromTemplate}")*/
    private TaskConfigurationProperties myProp;

    public InfoController(final DataSourceProperties dataSource, final TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    String url(){
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp(){
        return myProp.getTemplate().isAllowMultipleTasks();
    }


}
