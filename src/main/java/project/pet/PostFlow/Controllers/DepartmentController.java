package project.pet.PostFlow.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.Model.DTO.DepartmentDTORequest;
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
    public ResponseEntity<DepartmentDTORequest> createDepartment(@RequestBody DepartmentDTORequest departmentDTORequest) {
        return ResponseEntity.ok(departmentService.createDepartment(departmentDTORequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTORequest> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTORequest departmentDTORequest) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDTORequest));
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
