package net.javaguides.springboot.repository;

import net.javaguides.springboot.integration.AbstractContainerBaseTest;
import net.javaguides.springboot.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryITest extends AbstractContainerBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("sajid")
                .lastName("khokhar")
                .email("khokhar@email.com")
                .build();
    }

    @DisplayName("Junit test for the save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given - precondition or setup

        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("Junit test for find all employee list")
    @Test
    public void givenEmployeeList_whenFindAll_thenReturnSavedEmployeeList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder().firstName("sajid").lastName("khokhar").email("khokhar@email.com").build();
        employeeRepository.save(employee);
        employeeRepository.save(employee1);
        //when - action or the behavior that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();
        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);
        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);
        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        //given - precondition or setup
        employeeRepository.save(employee);
        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        String email = "sajidkhokhar@email.com";
        savedEmployee.setEmail(email);
        Employee updatedEmployee = employeeRepository.save(employee);
        //then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo(email);
    }

    @DisplayName("JUnit test for de;ete employee operation")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenReturnEmptyObject() {
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behavior that we are going to test
        //employeeRepository.delete(employee);
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> savedEmployee = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(savedEmployee).isEmpty();

    }

    @DisplayName("JUnit test for get employee by JPQL query using index parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLIndexParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        String firstName = "sajid";
        String lastName = "khokhar";
        employeeRepository.save(employee);
        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLIndexParams(firstName, lastName);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for get employee by JPQL query using name parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNameParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        String firstName = "sajid";
        String lastName = "khokhar";
        employeeRepository.save(employee);
        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for get employee by native SQL query using index parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLIndexParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        String firstName = "sajid";
        String lastName = "khokhar";
        employeeRepository.save(employee);
        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLIndexParams(firstName, lastName);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for get employee by Native SQL query using name parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNameParams_thenReturnEmployeeObject() {
        //given - precondition or setup
        String firstName = "sajid";
        String lastName = "khokhar";
        employeeRepository.save(employee);
        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(firstName, lastName);
        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}