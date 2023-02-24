package project.pet.PostFlow.Controllers;

import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Services.Service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @GetMapping("")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PostMapping("")
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployeesByDepartmentId(@PathVariable Long id) {
        return departmentService.getEmployeesByDepartmentId(id);
    }

    @GetMapping("/{id}/parcels")
    public List<Parcel> getParcelsByDepartmentId(@PathVariable Long id) {
        return departmentService.getParcelsByDepartmentId(id);
    }
}
