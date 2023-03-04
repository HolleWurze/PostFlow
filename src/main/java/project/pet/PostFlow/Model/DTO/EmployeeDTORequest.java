package project.pet.PostFlow.Model.DTO;

import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.Model.Entity.Department;

@Getter
@Setter
public class EmployeeDTORequest {
    Long id;
    String firstName;
    String lastName;
    Department department;
}
