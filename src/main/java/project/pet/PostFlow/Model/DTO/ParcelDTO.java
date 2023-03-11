package project.pet.PostFlow.Model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.Enum.Status;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Department;

@Getter
@Setter
public class ParcelDTO {
    @JsonProperty("id")
    Long id;
    Client client;
    Department department;
    Double weight;
    Status status;
    String trackingNumber;
    String description;
}

