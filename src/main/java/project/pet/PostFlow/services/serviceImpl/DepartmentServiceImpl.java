package project.pet.PostFlow.services.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.customException.AlreadyExistsException;
import project.pet.PostFlow.model.dto.DepartmentDTO;
import project.pet.PostFlow.model.entity.Department;
import project.pet.PostFlow.model.entity.Employee;
import project.pet.PostFlow.model.entity.Parcel;
import project.pet.PostFlow.model.repository.DepartmentRepository;
import project.pet.PostFlow.model.repository.EmployeeRepository;
import project.pet.PostFlow.model.repository.ParcelRepository;
import project.pet.PostFlow.services.service.DepartmentService;

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
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        departmentRepository.findById(departmentDTO.getId()).ifPresent(
                c -> {
                    throw new AlreadyExistsException("Отделение с таким ID уже существует ", HttpStatus.BAD_REQUEST);
                }
        );

        Department department = mapper.convertValue(departmentDTO, Department.class);
        Department save = departmentRepository.save(department);
        return mapper.convertValue(save, DepartmentDTO.class);

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
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отделение не найденно по текущему ID " + id));
        existingDepartment.setName(departmentDTO.getName());
        existingDepartment.setAddress(departmentDTO.getAddress());
        existingDepartment.setEmployees(departmentDTO.getEmployees());
        existingDepartment.setParcels(departmentDTO.getParcels());
        existingDepartment.setRequests(departmentDTO.getRequests());

        Department save = departmentRepository.save(existingDepartment);
        return mapper.convertValue(save, DepartmentDTO.class);
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
