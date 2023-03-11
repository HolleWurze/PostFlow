package project.pet.PostFlow.Model.DTO;

import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.Model.Entity.Request;

import java.util.List;

@Getter
@Setter
public class QueueDTO {
    Long id;
    Integer nextQueueNumber;
    Request currentRequest;
    List<Request> requests;
}
