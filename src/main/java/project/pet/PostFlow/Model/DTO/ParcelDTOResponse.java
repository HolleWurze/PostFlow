package project.pet.PostFlow.Model.DTO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParcelDTOResponse extends ParcelDTORequest{
    ParcelDTORequest parcelDTORequest;
}
