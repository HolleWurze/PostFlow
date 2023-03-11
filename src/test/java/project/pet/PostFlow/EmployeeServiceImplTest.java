package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import project.pet.PostFlow.Model.DTO.EmployeeDTO;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Model.Repository.DepartmentRepository;
import project.pet.PostFlow.Model.Repository.EmployeeRepository;
import project.pet.PostFlow.Services.ServiceImpl.EmployeeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

    @Spy
    private ObjectMapper mapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    public EmployeeServiceImpl employeeServiceImpl;

    @Test
    public void testCreateEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setFirstName("Вася");
        employeeDTO.setLastName("Пупкин");
        employeeDTO.setDepartment(departmentRepository.save(new Department()));

        when(employeeRepository.findById(employeeDTO.getId())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeDTO createdEmployee = employeeServiceImpl.createEmployee(employeeDTO);

        assertEquals(employeeDTO.getFirstName(), createdEmployee.getFirstName());
        assertEquals(employeeDTO.getLastName(), createdEmployee.getLastName());
        assertEquals(employeeDTO.getDepartment(), createdEmployee.getDepartment());

        verify(employeeRepository, times(1)).findById(employeeDTO.getId());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee() {
        Long id = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(id);
        employeeDTO.setFirstName("Вася");
        employeeDTO.setLastName("Пупкин");
        Department department = new Department();
        department.setName("Москва");
        department.setAddress("Лиговский 1");
        employeeDTO.setDepartment(department);

        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName("Анна");
        employee.setLastName("Ерохина");
        employee.setDepartment(new Department());
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        EmployeeDTO updatedEmployeeDTO = employeeServiceImpl.updateEmployee(id, employeeDTO);

        assertNotNull(updatedEmployeeDTO);
        assertEquals(id, updatedEmployeeDTO.getId());
        assertEquals(employeeDTO.getFirstName(), updatedEmployeeDTO.getFirstName());
        assertEquals(employeeDTO.getLastName(), updatedEmployeeDTO.getLastName());
        assertEquals(department.getName(), updatedEmployeeDTO.getDepartment().getName());
        assertEquals(department.getAddress(), updatedEmployeeDTO.getDepartment().getAddress());
        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).save(Mockito.any(Employee.class));
    }

    @Test
    public void testDeleteEmployee() {
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        employeeServiceImpl.deleteEmployee(id);

        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    public void testGetEmployeeById() {
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName("Вася");
        employee.setLastName("Пупкин");
        employee.setDepartment(new Department());

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeServiceImpl.getEmployeeById(id);

        assertNotNull(foundEmployee);
        assertEquals(id, foundEmployee.getId());
        assertEquals("Вася", foundEmployee.getFirstName());
        assertEquals("Пупкин", foundEmployee.getLastName());
        assertNotNull(foundEmployee.getDepartment());

        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        Employee employee = new Employee();
        Employee employee1 = new Employee();
        Department department = new Department();
        employee.setId(1L);
        employee.setFirstName("Вася");
        employee.setLastName("Пупкин");
        employee.setDepartment(department);
        employee1.setId(2L);
        employee1.setFirstName("Леся");
        employee1.setLastName("Укупник");
        employee1.setDepartment(department);
        employeeList.add(employee);
        employeeList.add(employee1);
        when(employeeRepository.findAll()).thenReturn(employeeList);

        List<Employee> result = employeeServiceImpl.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(employeeList.get(0).getId(), result.get(0).getId());
        assertEquals(employeeList.get(0).getFirstName(), result.get(0).getFirstName());
        assertEquals(employeeList.get(0).getLastName(), result.get(0).getLastName());
        assertEquals(employeeList.get(0).getDepartment(), result.get(0).getDepartment());
        assertEquals(employeeList.get(1).getId(), result.get(1).getId());
        assertEquals(employeeList.get(1).getFirstName(), result.get(1).getFirstName());
        assertEquals(employeeList.get(1).getLastName(), result.get(1).getLastName());
        assertEquals(employeeList.get(1).getDepartment(), result.get(1).getDepartment());
        verify(employeeRepository, times(1)).findAll();
    }


}
