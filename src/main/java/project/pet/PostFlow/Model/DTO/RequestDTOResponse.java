package project.pet.PostFlow.Model.DTO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDTOResponse extends RequestDTORequest {
    RequestDTORequest requestDTORequest;
}
