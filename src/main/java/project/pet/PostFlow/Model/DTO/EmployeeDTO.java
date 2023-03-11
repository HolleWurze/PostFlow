package project.pet.PostFlow.Model.DTO;

import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Model.Entity.Request;

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
