package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import project.pet.PostFlow.model.entity.Department;
import project.pet.PostFlow.model.entity.Employee;
import project.pet.PostFlow.model.entity.Parcel;
import project.pet.PostFlow.model.dto.DepartmentDTO;
import project.pet.PostFlow.model.repository.DepartmentRepository;
import project.pet.PostFlow.model.repository.EmployeeRepository;
import project.pet.PostFlow.model.repository.ParcelRepository;

import project.pet.PostFlow.services.serviceImpl.DepartmentServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceImplTest {
    @Spy
    private ObjectMapper mapper;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ParcelRepository parcelRepository;
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    public void testCreateDepartment() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Отделение 1");
        departmentDTO.setAddress("Москва");

        when(departmentRepository.findById(departmentDTO.getId())).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartmentDTO createdDepartmentDTO = departmentService.createDepartment(departmentDTO);

        assertEquals(departmentDTO.getName(), createdDepartmentDTO.getName());
        assertEquals(departmentDTO.getAddress(), createdDepartmentDTO.getAddress());

        verify(departmentRepository, times(1)).findById(departmentDTO.getId());

        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    public void testGetAllDepartments() {
        Department department1 = new Department();
        department1.setName("Отделение 1");
        department1.setAddress("Москва");

        Department department2 = new Department();
        department2.setName("Отделение 2");
        department2.setAddress("Санкт-Петербург");

        List<Department> departments = Arrays.asList(department1, department2);

        when(departmentRepository.findAll()).thenReturn(departments);

        List<Department> result = departmentService.getAllDepartments();

        verify(departmentRepository, times(1)).findAll();

        assertEquals(departments, result);
    }

    @Test
    public void testGetDepartmentById() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Отделение 1");
        department.setAddress("Москва");

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));

        Department foundDepartment = departmentService.getDepartmentById(department.getId());

        verify(departmentRepository, times(1)).findById(department.getId());

        assertEquals(department, foundDepartment);
    }

    @Test
    public void testUpdateDepartment() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Отделение 1");
        departmentDTO.setAddress("Москва");

        Department existingDepartment = new Department();
        existingDepartment.setId(1L);
        existingDepartment.setName("Отделение 1");
        existingDepartment.setAddress("Владивосток");

        when(departmentRepository.findById(existingDepartment.getId())).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartmentDTO updatedDepartmentDTO = departmentService.updateDepartment(existingDepartment.getId(), departmentDTO);

        assertEquals(departmentDTO.getName(), updatedDepartmentDTO.getName());
        assertEquals(departmentDTO.getAddress(), updatedDepartmentDTO.getAddress());

        verify(departmentRepository, times(1)).findById(existingDepartment.getId());

        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    public void testDeleteDepartment() {
        Department department = new Department();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).findById(1L);

        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    public void testGetEmployeesByDepartmentId() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        when(employeeRepository.findByDepartmentId(1L)).thenReturn(employees);

        List<Employee> result = departmentService.getEmployeesByDepartmentId(1L);

        verify(employeeRepository, times(1)).findByDepartmentId(1L);

        assertEquals(employees, result);
    }

    @Test
    public void testGetParcelsByDepartmentId() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(new Parcel());
        when(parcelRepository.findByDepartmentId(1L)).thenReturn(parcels);

        List<Parcel> result = departmentService.getParcelsByDepartmentId(1L);

        verify(parcelRepository, times(1)).findByDepartmentId(1L);

        assertEquals(parcels, result);
    }
}







