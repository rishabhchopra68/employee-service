package com.codingshuttle.testingApp.TestingApp.repository;

import com.codingshuttle.testingApp.TestingApp.TestContainerConfiguration;
import com.codingshuttle.testingApp.TestingApp.entities.EmployeeEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Import(TestContainerConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    private EmployeeEntity employee;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void createEmployee(){
        employee = EmployeeEntity.builder()
                .name("Rishabh")
                .email("rishabh68@gmail.com")
                .salary(100L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
        employeeRepository.save(employee);
        List<EmployeeEntity> employees = employeeRepository.findByEmail(employee.getEmail());
        assertThat(employees).isNotNull();
        assertThat(employees).isNotEmpty();
        assertThat(employees.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList() {
        // Arrange
        String email = "test@gmail.com";

        // Act
        List<EmployeeEntity> employees = employeeRepository.findByEmail(email);

        //Assert
        assertThat(employees).isNotNull();
        assertThat(employees).isEmpty();
    }
}