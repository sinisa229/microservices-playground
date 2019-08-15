package com.microservices.playground.playground.service2;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@RestController
@SpringBootApplication
@EnableSwagger2
@EnableAsync
@Slf4j
public class MyService2 {

    @Autowired
	private ApplicationEventPublisher applicationEventPublisher;

    public static void main(String[] args) {
		SpringApplication.run(MyService2.class, args);
	}

    @GetMapping("sleuth")
    public String sleuth() {
		log.info("before sleuthing");
        final String sleuthing = "Sleuth-ing from service 2";
		applicationEventPublisher.publishEvent(new Event("someContent"));
        log.info(sleuthing);
		return sleuthing;
    }

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("MyService2")
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build().apiInfo(new ApiInfo("Microservices playground", "Microservices playground", "", "", null, "", "", new ArrayList<>()));
	}

	@Async
	@EventListener
	public void onApplicationEvent(Event event) {
		log.info("{}", event);
	}
}

@Value
class Event {
	private final String eventContent;
}