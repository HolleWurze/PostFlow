package project.pet.PostFlow.model.dto;

import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.entity.Employee;
import project.pet.PostFlow.model.entity.Parcel;
import project.pet.PostFlow.model.entity.Request;

import java.util.List;

@Getter
@Setter
public class DepartmentDTO {
    Long id;
    String name;
    String address;
    List<Employee> employees;
    List<Parcel> parcels;
    List<Request> requests;
    List<Client> clients;
}
