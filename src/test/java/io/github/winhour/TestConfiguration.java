package io.github.winhour;

import io.github.winhour.model.TaskRepository;
import io.github.winhour.model.TestTaskRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eTestDataSource() {
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1","","");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }


    @Bean
    @Primary
    //@ConditionalOnMissingBean
    @Profile({"integration"/*, "!prod"*/})
    TaskRepository testRepo(){
        return new TestTaskRepository();
    }


}
