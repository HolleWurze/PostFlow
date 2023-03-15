package project.pet.PostFlow.model.dto;

import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.model.entity.Department;
import project.pet.PostFlow.model.entity.Parcel;
import project.pet.PostFlow.model.entity.Request;

import java.util.List;

@Getter
@Setter
public class EmployeeDTO {
    Long id;
    String firstName;
    String lastName;
    Department department;
    List<Parcel> parcel;
    List<Request> requests;
}
