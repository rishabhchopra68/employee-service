package com.codingshuttle.testingApp.TestingApp.services.impl;

import com.codingshuttle.testingApp.TestingApp.TestContainerConfiguration;
import com.codingshuttle.testingApp.TestingApp.dto.EmployeeDTO;
import com.codingshuttle.testingApp.TestingApp.entities.EmployeeEntity;
import com.codingshuttle.testingApp.TestingApp.exceptions.ResourceNotFoundException;
import com.codingshuttle.testingApp.TestingApp.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

//@DataJpaTest // only valid for repo and entity related code
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfiguration.class) // It only has an effect when a Spring ApplicationContext is created for the test, which happens with @SpringBootTest, @DataJpaTest, and similar
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock   // these will be injected inside the EmployeeServiceImpl instance
    private EmployeeRepository employeeRepository;

    @Spy    // using real modelmapper since its 3rd party library. using real bean
    private ModelMapper modelMapper;

//    @Autowired
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeEntity mockEmployee;
    private EmployeeDTO mockEmployeeDto;

    @BeforeEach
    void setup(){
        mockEmployee = EmployeeEntity.builder()
                .id(1L)
                .name("rishabh")
                .email("rishabhchopra68@gmail.com")
                .salary(200L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee, EmployeeDTO.class);
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDTO(){
//        employeeService.getEmployeeById(1L);
        // assign
        Long id = mockEmployee.getId();
        when(employeeRepository.findById(id))   // stubbing
                .thenReturn(Optional.of(mockEmployee));
        // act
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);

        //assert
        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getId()).isEqualTo(id);
        assertThat(employeeDTO.getEmail()).isEqualTo(mockEmployee.getEmail());

        verify(employeeRepository, atLeast(1)).findById(id); // was called
//        verify(employeeRepository).save(null); // was called
        verify(employeeRepository, only()).findById(id); // was called
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIsNotPresent_ThenThrowException(){
        //arrange
        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        //act and assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee(){
        //assign
        when(employeeRepository.findByEmail(anyString())) //testing happy scenario
                .thenReturn(List.of());
        when(employeeRepository.save(any(EmployeeEntity.class))) //testing happy scenario
                .thenReturn(mockEmployee);

        //act
        EmployeeDTO employeeDTO = employeeService.createNewEmployee(mockEmployeeDto);

        //assert
        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

        ArgumentCaptor<EmployeeEntity> employeeArgumentCaptor = ArgumentCaptor.forClass(EmployeeEntity.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        EmployeeEntity capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }

    @Test
    void testCreateNewEmployee_WhenEmployeeAlreadyExistsWithEmail_ThenThrowException(){
        //arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())) //testing happy scenario
                .thenReturn(List.of(mockEmployee));

        //act and assert
        assertThatThrownBy(() -> employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+ mockEmployee.getEmail());
        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenEmployeeDoesNotExist_ThenThrowException(){
        //arrange
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());
        // act and assert
        assertThatThrownBy(() -> employeeService.updateEmployee(1L, mockEmployeeDto ))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenAttemptingToUpdateEmail_ThenThrowException(){
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setEmail("random@gamil.com");
        mockEmployeeDto.setName("random");


        assertThatThrownBy(() -> employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenValidEmployee_ThenUpdateEmployee(){
        //arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("random");
        mockEmployeeDto.setSalary(299L);

        EmployeeEntity updatedEmployee = modelMapper.map(mockEmployeeDto, EmployeeEntity.class);
        when(employeeRepository.save(any(EmployeeEntity.class)))
                .thenReturn(updatedEmployee);

        //act
        EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto);
        assertThat(updatedEmployeeDTO).isEqualTo(mockEmployeeDto);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any());
    }

    @Test
    void testDeleteEmployee_WhenEmployeeDoesNotExist_ThenThrowException(){
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository, never()).deleteById(anyLong()) ;
    }

    @Test
    void testDeleteEmployee_WhenEmployeeExists_ThenDeleteEmployee(){
        when(employeeRepository.existsById(1L)).thenReturn(true);

        assertThatCode(() -> employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();
        verify(employeeRepository).deleteById(1L) ;
    }
}