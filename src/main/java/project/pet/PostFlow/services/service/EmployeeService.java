package project.pet.PostFlow.services.service;

import project.pet.PostFlow.model.dto.EmployeeDTO;
import project.pet.PostFlow.model.entity.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
    Employee getEmployeeById(Long id);
    List<Employee> getAllEmployees();
}
