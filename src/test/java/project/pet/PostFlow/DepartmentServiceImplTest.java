package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Model.DTO.DepartmentDTORequest;
import project.pet.PostFlow.Model.Repository.DepartmentRepository;
import project.pet.PostFlow.Model.Repository.EmployeeRepository;
import project.pet.PostFlow.Model.Repository.ParcelRepository;
import project.pet.PostFlow.Services.ServiceImpl.DepartmentServiceImpl;

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
        DepartmentDTORequest departmentDTORequest = new DepartmentDTORequest();
        departmentDTORequest.setName("Отделение 1");
        departmentDTORequest.setAddress("Москва");

        when(departmentRepository.findById(departmentDTORequest.getId())).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartmentDTORequest createdDepartmentDTO = departmentService.createDepartment(departmentDTORequest);

        assertEquals(departmentDTORequest.getName(), createdDepartmentDTO.getName());
        assertEquals(departmentDTORequest.getAddress(), createdDepartmentDTO.getAddress());

        verify(departmentRepository, times(1)).findById(departmentDTORequest.getId());

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
        DepartmentDTORequest departmentDTORequest = new DepartmentDTORequest();
        departmentDTORequest.setName("Отделение 1");
        departmentDTORequest.setAddress("Москва");

        Department existingDepartment = new Department();
        existingDepartment.setId(1L);
        existingDepartment.setName("Отделение 1");
        existingDepartment.setAddress("Владивосток");

        when(departmentRepository.findById(existingDepartment.getId())).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartmentDTORequest updatedDepartmentDTO = departmentService.updateDepartment(existingDepartment.getId(), departmentDTORequest);

        assertEquals(departmentDTORequest.getName(), updatedDepartmentDTO.getName());
        assertEquals(departmentDTORequest.getAddress(), updatedDepartmentDTO.getAddress());

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







