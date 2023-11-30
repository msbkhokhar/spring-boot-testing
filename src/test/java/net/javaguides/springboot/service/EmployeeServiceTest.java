package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;

    @BeforeEach
    public void setup() {
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("sajid")
                .lastName("khokhar")
                .email("khokhar@email.com")
                .build();
    }

    @DisplayName("Junit test for the save employee method")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        System.out.println(employeeRepository);
        System.out.println(employeeService);
        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Junit test for the saveemployee method throws exception")
    @Test
    public void givenExistingEmployeeEmail_whenSave_thenThrowsException() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
        //when - action or the behavior that we are going to test
        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            employeeService.saveEmployee(employee);
        });
        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("Junit test for the getAllEmployees method")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("sajid")
                .lastName("khokhar")
                .email("khokhar@email.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));
        //when - action or the behavior that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("Junit test for the getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when - action or the behavior that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("Junit test for the getEmployeeById method")
    @Test
    public void givenEmployeeId_whenFindById_thenReturnEmptyObject() {
        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        //when - action or the behavior that we are going to test
        Employee employeeObject = employeeService.getEmployeeById(employee.getId()).get();
        //then - verify the output
        assertThat(employeeObject).isNotNull();
    }
    @DisplayName("Junit test for the updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        //when - action or the behavior that we are going to test
        String email = "sajidkhokhar@email.com";
        employee.setEmail(email);
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo(email);
    }
    @DisplayName("Junit test for the deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        //given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);
        //when - action or the behavior that we are going to test
        employeeService.deleteEmployee(employeeId);
        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}