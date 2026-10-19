package com.codingshuttle.testingApp.TestingApp.services;

import com.codingshuttle.testingApp.TestingApp.dto.EmployeeDTO;

public interface EmployeeService {

    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO createNewEmployee(EmployeeDTO employeeDto);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDto);
    void deleteEmployee(Long id);
}
