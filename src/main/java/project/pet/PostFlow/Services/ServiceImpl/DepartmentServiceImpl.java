package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.CustomException.AlreadyExistsException;
import project.pet.PostFlow.Model.DTO.DepartmentDTORequest;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Model.Repository.DepartmentRepository;
import project.pet.PostFlow.Model.Repository.EmployeeRepository;
import project.pet.PostFlow.Model.Repository.ParcelRepository;
import project.pet.PostFlow.Services.Service.DepartmentService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ParcelRepository parcelRepository;
    private final ObjectMapper mapper;

    @Override
    public DepartmentDTORequest createDepartment(DepartmentDTORequest departmentDTORequest) {
        departmentRepository.findById(departmentDTORequest.getId()).ifPresent(
                c -> {
                    throw new AlreadyExistsException("Отделение с таким ID уже существует ", HttpStatus.BAD_REQUEST);
                }
        );

        Department department = mapper.convertValue(departmentDTORequest, Department.class);
        Department save = departmentRepository.save(department);
        return mapper.convertValue(save, DepartmentDTORequest.class);

    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отделение не найденно по текущему ID " + id));
    }

    @Override
    public DepartmentDTORequest updateDepartment(Long id, DepartmentDTORequest departmentDTORequest) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отделение не найденно по текущему ID " + id));
        existingDepartment.setName(departmentDTORequest.getName());
        existingDepartment.setAddress(departmentDTORequest.getAddress());
        existingDepartment.setEmployees(departmentDTORequest.getEmployees());
        existingDepartment.setParcels(departmentDTORequest.getParcels());
        existingDepartment.setRequests(departmentDTORequest.getRequests());

        Department save = departmentRepository.save(existingDepartment);
        return mapper.convertValue(save, DepartmentDTORequest.class);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отделение не найденно по текущему ID " + id));
        departmentRepository.delete(department);
    }

    @Override
    public List<Employee> getEmployeesByDepartmentId(Long id) {
        return employeeRepository.findByDepartmentId(id);
    }

    @Override
    public List<Parcel> getParcelsByDepartmentId(Long id) {
        return parcelRepository.findByDepartmentId(id);
    }
}
