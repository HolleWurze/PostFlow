package project.pet.PostFlow.model.dto;

import lombok.Getter;
import lombok.Setter;
import project.pet.PostFlow.model.entity.Request;

import java.util.List;

@Getter
@Setter
public class QueueDTO {
    Long id;
    Integer nextQueueNumber;
    Request currentRequest;
    List<Request> requests;
}
