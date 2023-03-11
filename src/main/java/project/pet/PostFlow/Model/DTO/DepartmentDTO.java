package project.pet.PostFlow.Model.DTO;

import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Employee;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Model.Entity.Request;

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
