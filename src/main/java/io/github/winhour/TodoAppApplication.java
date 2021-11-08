package io.github.winhour;

import io.github.winhour.model.TaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

/*@EnableAsync*/
@SpringBootApplication
//@ComponentScan(basePackages = "db.migration")
//@Import(TaskConfigurationProperties,class)
public class TodoAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(TodoAppApplication.class, args);

	}

	@Bean
	Validator validator(/*TaskRepository repository*/) {
		/*return repository.findById(1)
				.map((task) -> new LocalValidatorFactoryBean())
				.orElse(null);*/
		return new LocalValidatorFactoryBean();
	}

	/*
	@Override
	public void configureValidatingRepositoryEventListener(final ValidatingRepositoryEventListener validatingListener) {

		validatingListener.addValidator("beforeCreate", validator());
		validatingListener.addValidator("beforeSave", validator());

	}
	*/

}
