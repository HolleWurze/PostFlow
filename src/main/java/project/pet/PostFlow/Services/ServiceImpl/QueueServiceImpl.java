package project.pet.PostFlow.Services.ServiceImpl;

import org.springframework.stereotype.Service;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.QueueRepository;
import project.pet.PostFlow.Services.Service.ClientService;
import project.pet.PostFlow.Services.Service.QueueService;
import project.pet.PostFlow.Services.Service.RequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueueServiceImpl implements QueueService {

    private final Map<Client, Request> queue = new LinkedHashMap<>();
    private final List<Client> priorityClients = new ArrayList<>();
    private int priorityQueueSize = 0;

    private final QueueRepository queueRepository;
    private final ClientService clientService;
    private final RequestService requestService;

    public QueueServiceImpl(QueueRepository queueRepository, ClientService clientService, RequestService requestService) {
        this.queueRepository = queueRepository;
        this.clientService = clientService;
        this.requestService = requestService;
    }


    @Override
    public Request addRequest(Client client, RequestType requestType, LocalDateTime appointmentTime) {
        return null;
    }

    @Override
    public Request getCurrentRequest() {
        return null;
    }

    @Override
    public void markCurrentRequestDone() {

    }

    @Override
    public List<Request> getRequests() {
        return new ArrayList<>(queue.values());
    }

    @Override
    public void removeRequest(Request request) {

    }

    @Override
    public void recalculateEstimatedTime() {

    }

    @Override
    public void removeFromQueue(Client client) {

    }

    @Override
    public List<Client> getClientQueue() {
        return new ArrayList<>(queue.keySet());
    }
}
