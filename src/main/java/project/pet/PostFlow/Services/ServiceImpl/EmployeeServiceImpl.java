package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.CustomException.AlreadyExistsException;
import project.pet.PostFlow.CustomException.ResourceNotFoundException;
import project.pet.PostFlow.Model.DTO.EmployeeDTO;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Model.Repository.EmployeeRepository;
import project.pet.PostFlow.Services.Service.EmployeeService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ObjectMapper mapper;

    @Override
    public EmployeeDTO createEmployee(@NotNull EmployeeDTO employeeDTO) {
        employeeRepository.findById(employeeDTO.getId()).ifPresent(
                c -> {
                    throw new AlreadyExistsException("Сотрудник с таким ID уже существует ", HttpStatus.BAD_REQUEST);
                }
        );

        Employee employee = mapper.convertValue(employeeDTO, Employee.class);
        Employee save = employeeRepository.save(employee);
        return mapper.convertValue(save, EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setDepartment(employeeDTO.getDepartment());

        Employee save = employeeRepository.save(employee);
        return mapper.convertValue(save, EmployeeDTO.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        employeeRepository.delete(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
