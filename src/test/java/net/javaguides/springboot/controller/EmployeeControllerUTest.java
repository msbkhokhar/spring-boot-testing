package net.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerUTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("sajid")
                .lastName("khokhar")
                .email("khokhar@email.com")
                .build();
    }

    @Test
    public void givenEmployeeObject_WhencreateEmployee_ThenReturnSavenEmployee() throws Exception {
        //Given
        BDDMockito.given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect((jsonPath("$.lastName", is(employee.getLastName()))))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andDo(print());

    }

    @DisplayName("Junit Test for getAllEmployees REST API")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(employee);
        listOfEmployees.add(Employee.builder().firstName("sajid").lastName("bilal khokhar").email("sajidkhokhar@email.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employee"));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @DisplayName("Junit Test for getEmployeeById REST API (+ scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeesObject() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        employee.setId(employeeId);
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employee/{id}",employeeId));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("Junit Test for getAllEmployeeById REST API (- scenario)")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenOptionalEmpty() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        employee.setId(employeeId);
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employee/{id}",employeeId));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Junit Test for updateEmployee REST API (+ scenario)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeesObj() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        employee.setId(employeeId);
        Employee updatedEmployee = Employee.builder()
                .firstName("sajid")
                .lastName("khokhar")
                .email("sajidkhokhar@email.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation)->invocation.getArgument(0));
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employee/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Junit Test for updateEmployee REST API (- scenario)")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnNotFoundError() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        employee.setId(employeeId);
        Employee updatedEmployee = Employee.builder()
                .firstName("sajid")
                .lastName("khokhar")
                .email("sajidkhokhar@email.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation)->invocation.getArgument(0));
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employee/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }
    @DisplayName("Junit Test for deleteEmployee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnString() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;
        employee.setId(employeeId);
        willDoNothing().given(employeeService).deleteEmployee(employeeId);
        //when - action or the behavior that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employee/{id}", employeeId));
        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }

}