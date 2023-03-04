package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.CustomException.AlreadyExistsException;
import project.pet.PostFlow.CustomException.ResourceNotFoundException;
import project.pet.PostFlow.Model.DTO.EmployeeDTORequest;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Model.Repository.EmployeeRepository;
import project.pet.PostFlow.Services.Service.EmployeeService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private final ObjectMapper mapper;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    public EmployeeDTORequest createEmployee(EmployeeDTORequest employeeDTORequest) {
        employeeRepository.findById(employeeDTORequest.getId()).ifPresent(
                c -> {
                    throw new AlreadyExistsException("Сотрудник с таким ID уже существует ", HttpStatus.BAD_REQUEST);
                }
        );

        Employee employee = mapper.convertValue(employeeDTORequest, Employee.class);
        Employee save = employeeRepository.save(employee);
        return mapper.convertValue(save, EmployeeDTORequest.class);
    }

    public EmployeeDTORequest updateEmployee(Long id, EmployeeDTORequest employeeDTORequest) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employee.setFirstName(employeeDTORequest.getFirstName());
        employee.setLastName(employeeDTORequest.getLastName());
        employee.setDepartment(employeeDTORequest.getDepartment());

        Employee save = employeeRepository.save(employee);
        return mapper.convertValue(save, EmployeeDTORequest.class);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        employeeRepository.delete(employee);
    }
}
