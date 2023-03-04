package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.DTO.EmployeeDTORequest;
import project.pet.PostFlow.Model.Entity.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeDTORequest createEmployee(EmployeeDTORequest employeeDTORequest);
    EmployeeDTORequest updateEmployee(Long id, EmployeeDTORequest employeeDTORequest);
    void deleteEmployee(Long id);
    Employee getEmployeeById(Long id);
    List<Employee> getAllEmployees();
}
