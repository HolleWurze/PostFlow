package project.pet.PostFlow.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.CustomException.ResourceNotFoundException;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Services.Service.EmployeeService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId, @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException {
        employeeService.deleteEmployee(employeeId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
