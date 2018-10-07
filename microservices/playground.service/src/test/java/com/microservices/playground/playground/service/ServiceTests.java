package com.microservices.playground.playground.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTests {

    @Value("${application.version}")
    private String applicationVersion;

	@Test
	public void contextLoads() {
        System.out.println(applicationVersion);
	}

}
