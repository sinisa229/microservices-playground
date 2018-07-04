package com.microservices.playground.playground.service2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
public class MyService2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyService2.class);

    public static void main(String[] args) {
		SpringApplication.run(MyService2.class, args);
	}

    @GetMapping("sleuth")
    public String sleuth() {
        final String sleuthing = "Sleuth-ing from service 2";
        LOGGER.info(sleuthing);
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

}
