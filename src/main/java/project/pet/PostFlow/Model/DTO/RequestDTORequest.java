package project.pet.PostFlow.Model.DTO;

import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Parcel;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestDTORequest {
    Long id;
    Department department;
    Parcel parcel;
    Client client;
    String appointmentTime;
    String waitingTime;
    String estimatedTime;
    RequestType requestType;

}

