package io.github.winhour.logic;

import io.github.winhour.TaskConfigurationProperties;
import io.github.winhour.model.ProjectRepository;
import io.github.winhour.model.TaskGroupRepository;
import io.github.winhour.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LogicConfiguration {

    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties config,
            final TaskGroupService service
    ){
        return new ProjectService(repository, config, taskGroupRepository, service);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository tgRepository,
            final TaskRepository repository
    ){
        return new TaskGroupService(tgRepository, repository);
    }

}
