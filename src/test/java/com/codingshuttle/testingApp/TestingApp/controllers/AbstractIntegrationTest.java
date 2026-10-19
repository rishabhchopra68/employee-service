package com.codingshuttle.testingApp.TestingApp.controllers;

import com.codingshuttle.testingApp.TestingApp.TestContainerConfiguration;
import com.codingshuttle.testingApp.TestingApp.dto.EmployeeDTO;
import com.codingshuttle.testingApp.TestingApp.entities.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    EmployeeEntity testEmployee = EmployeeEntity.builder()
                .email("anuj@gmail.com")
                .name("Anuj")
                .salary(200L)
                .build();
    EmployeeDTO testEmployeeDto = EmployeeDTO.builder()
            .id(1L)
                .email("anuj@gmail.com")
                .name("Anuj")
                .salary(200L)
                .build();
}
