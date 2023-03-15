package project.pet.PostFlow.services.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.customException.AlreadyExistsException;
import project.pet.PostFlow.customException.ResourceNotFoundException;
import project.pet.PostFlow.model.dto.EmployeeDTO;
import project.pet.PostFlow.model.entity.Employee;
import project.pet.PostFlow.model.repository.EmployeeRepository;
import project.pet.PostFlow.services.service.EmployeeService;

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
