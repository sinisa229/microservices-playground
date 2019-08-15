package com.microservices.playground.playground.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
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

@SpringBootApplication
@RestController
@EnableJpaRepositories
@EnableFeignClients
@EnableSwagger2
public class MyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyService.class);

    @Value("application.version")
    private String applicationVersion;

	@Autowired
	private PersonRepository personRepository;
	@Autowired
    private FailingService failingService;
	@Autowired
    private MyService2Client service2Client;

    public static void main(String[] args) {
		SpringApplication.run(MyService.class, args);
	}

	@GetMapping("person")
	public Person getPerson(Long id) {
		return personRepository.findById(id).orElseGet(Person::new);
	}

	@PostMapping("person")
	public Person savePerson(Person person) {
		return personRepository.save(person);
	}

    @PostMapping("fail")
    public String fail() {
        return failingService.fail();
    }

    @GetMapping("sleuth")
    public String sleuth() {
        LOGGER.info("Sleuth-ing");
        LOGGER.info("Sleuth-ing 2");
        return service2Client.sleuth();
    }

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("MyService")
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build().apiInfo(new ApiInfo("Microservices playground", "Microservices playground", "", "", null, "", "", new ArrayList<>()));
	}

}

@Service
@EnableCircuitBreaker
class FailingService {

    @HystrixCommand(fallbackMethod = "failRecovery")
    public String fail() {
        throw new RuntimeException("Failing");
    }

    public String failRecovery() {
        return "Failure success";
    }

}

@FeignClient(value = "localhost", url = "http://localhost:8091")
interface MyService2Client {
    @RequestMapping(method = RequestMethod.GET, value = "/sleuth")
    String sleuth();
}

interface PersonRepository extends CrudRepository<Person, Long> {

}

@Slf4j
@ControllerAdvice
class  MyControllerAdvice {

    public @ResponseBody String handle(Exception e) {
        log.error("Err", e);
        return "{}";
    }

}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
}
