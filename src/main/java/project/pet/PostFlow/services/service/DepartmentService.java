package project.pet.PostFlow.services.service;

import project.pet.PostFlow.model.dto.DepartmentDTO;
import project.pet.PostFlow.model.entity.Department;
import project.pet.PostFlow.model.entity.Employee;
import project.pet.PostFlow.model.entity.Parcel;

import java.util.List;

public interface DepartmentService {
    Department getDepartmentById(Long id);
    List<Department> getAllDepartments();
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);
    void deleteDepartment(Long id);
    List<Employee> getEmployeesByDepartmentId(Long id);
    List<Parcel> getParcelsByDepartmentId(Long id);
}
