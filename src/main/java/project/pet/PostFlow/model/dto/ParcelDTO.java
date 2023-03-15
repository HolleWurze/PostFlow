package project.pet.PostFlow.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.enums.Status;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.entity.Department;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class ParcelDTO {
    @JsonProperty("id")
    Long id;
    Client client;
    Department department;
    Double weight;
    @Enumerated(EnumType.STRING)
    Status status;
    String trackingNumber;
    String description;
}

