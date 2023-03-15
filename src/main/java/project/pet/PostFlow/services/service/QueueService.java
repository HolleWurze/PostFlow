package project.pet.PostFlow.services.service;

import project.pet.PostFlow.enums.RequestType;
import project.pet.PostFlow.model.dto.ClientDTO;
import project.pet.PostFlow.model.dto.RequestDTO;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.entity.Request;

import java.util.List;

public interface QueueService {
    int AVERAGE_WAITING_TIME_IN_MINUTES = 5;
    RequestDTO addRequest(ClientDTO clientDTO, RequestType requestType, String appointmentTime);
    RequestDTO getCurrentRequest();
    void markCurrentRequestDone();
    List<Request> getRequests();
    void removeRequest(Request request);
    void recalculateEstimatedTime(Client client);
    void removeFromQueue(Client client);
    List<Client> getClientQueue();
}
