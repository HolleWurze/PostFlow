package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.DTO.EmployeeDTO;
import project.pet.PostFlow.Model.Entity.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
    Employee getEmployeeById(Long id);
    List<Employee> getAllEmployees();
}
