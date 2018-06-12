package com.microservices.playground.playground.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("person")
	public Person getPerson(Long id) {
		return new Person("FirstName", "LastName");
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class Person {
		private String firstName;
		private String lastName;
	}

}
