package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.DTO.DepartmentDTORequest;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Model.Entity.Parcel;

import java.util.List;


public interface DepartmentService {
    Department getDepartmentById(Long id);
    List<Department> getAllDepartments();
    DepartmentDTORequest createDepartment(DepartmentDTORequest departmentDTORequest);
    DepartmentDTORequest updateDepartment(Long id, DepartmentDTORequest departmentDTORequest);
    void deleteDepartment(Long id);
    List<Employee> getEmployeesByDepartmentId(Long id);
    List<Parcel> getParcelsByDepartmentId(Long id);
}
