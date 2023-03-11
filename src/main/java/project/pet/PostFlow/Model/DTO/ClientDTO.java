package project.pet.PostFlow.Model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.Enum.CRUDStatus;
import project.pet.PostFlow.Enum.ClientPriority;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Model.Entity.Request;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ClientDTO {
    Long id;
    String firstName;
    String lastName;
    Department department;
    @JsonProperty("createdAt")
    LocalDateTime createdAt;
    @JsonProperty("updatedAt")
    LocalDateTime updatedAt;
    @JsonProperty("deletedAt")
    LocalDateTime deletedAt;
    CRUDStatus status;
    List<Parcel> parcels;
    List<Request> requests;
    ClientPriority clientPriority;
}
