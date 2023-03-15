package project.pet.PostFlow.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.enums.RequestType;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.entity.Department;
import project.pet.PostFlow.model.entity.Parcel;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestDTO {
    Long id;
    Department department;
    Parcel parcel;
    Client client;
    String appointmentTime;
    String waitingTime;
    String estimatedTime;
    @Enumerated(EnumType.STRING)
    RequestType requestType;

}

