package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.firstName =?1 and e.lastName = ?2")
    Employee findByJPQLIndexParams(String firstName, String lastName);

    @Query("select e from Employee e where e.firstName =:firstName and e.lastName = :lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(value = "select * from employees e where e.first_name =?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByNativeSQLIndexParams(String firstName, String lastName);

    @Query(value ="select * from employees e where e.first_name =:firstName and e.last_name = :lastName", nativeQuery = true)
    Employee findByNativeSQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
