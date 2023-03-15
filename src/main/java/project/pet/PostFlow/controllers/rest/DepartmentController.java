package project.pet.PostFlow.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.model.dto.DepartmentDTO;
import project.pet.PostFlow.model.entity.Department;
import project.pet.PostFlow.model.entity.Employee;
import project.pet.PostFlow.model.entity.Parcel;
import project.pet.PostFlow.services.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @GetMapping("")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PostMapping("")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.ok(departmentService.createDepartment(departmentDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDTO));
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
