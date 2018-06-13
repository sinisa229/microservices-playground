package com.microservices.playground.playground.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;

@RestController
@EnableJpaRepositories(considerNestedRepositories = true)
@SpringBootApplication
@EnableSwagger2
public class Application {

	@Autowired
	private PersonRepository personRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("person")
	public Person getPerson(Long id) {
		return personRepository.findById(id).orElseGet(Person::new);
	}

	@PostMapping("person")
	public Person savePerson(Person person) {
		return personRepository.save(person);
	}

	interface PersonRepository extends CrudRepository<Person, Long> {

	}

	@Entity
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class Person {
		@Id
		@GeneratedValue
		private Long id;
		private String firstName;
		private String lastName;
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Service")
				.select()
				.paths(input -> input.contains("person"))
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build().apiInfo(new ApiInfo("Microservices playground", "Microservices playground", "", "", null, "", "", new ArrayList<>()));
	}

}
