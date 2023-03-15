package project.pet.PostFlow.model.dto;

import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.enums.ClientPriority;
import project.pet.PostFlow.model.entity.Department;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class ClientDTO {
    Long id;
    String firstName;
    String lastName;
    Department department;
    String userName;
    String password;
    String email;
    @Enumerated(EnumType.STRING)
    ClientPriority clientPriority;
}
