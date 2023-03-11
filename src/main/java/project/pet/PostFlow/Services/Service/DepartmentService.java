package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.DTO.DepartmentDTO;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Model.Entity.Parcel;

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
