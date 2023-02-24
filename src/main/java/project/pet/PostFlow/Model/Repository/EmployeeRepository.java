package project.pet.PostFlow.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.pet.PostFlow.Model.Entity.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long departmentId);
}
