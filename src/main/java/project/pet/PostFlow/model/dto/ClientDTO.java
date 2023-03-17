package project.pet.PostFlow.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.enums.ClientPriority;
import project.pet.PostFlow.model.entity.Department;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDTO {
    Long id;
    String firstName;
    String lastName;
    Department department;
    @NotBlank
    String userName;
    String password;
    @Email
    String email;
    @Enumerated(EnumType.STRING)
    ClientPriority clientPriority;
}
