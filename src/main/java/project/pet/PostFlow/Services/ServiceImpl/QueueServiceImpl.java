package project.pet.PostFlow.Services.ServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.Enum.ClientPriority;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Queue;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.QueueRepository;
import project.pet.PostFlow.Services.Service.QueueService;
import project.pet.PostFlow.Services.Service.RequestService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final Map<Client, Request> queue = new LinkedHashMap<>();
    private final QueueRepository queueRepository;
    //private final ClientService clientService;
    private final RequestService requestService;
    private final int averageWaitingTimeInMinutes;


    @Override
    public Request addRequest(ClientDTORequest clientDTORequest, RequestType requestType, String appointmentTime) {
        // Если у клиента приоритетный статус то мы его сразу обслуживаем
        if (clientDTORequest.getClientPriority() == ClientPriority.PRIORITY) {
            Request request = requestService.createRequest(clientDTORequest, requestType, appointmentTime);
            request.setWaitingTime(String.valueOf(Duration.ZERO));
            return request;
        }

        Queue queue = getOrCreateQueue();
        Request request = requestService.createRequest(clientDTORequest, requestType, appointmentTime);
        queue.getRequests().add(request);
        queue.setNextQueueNumber(queue.getNextQueueNumber() + 1);
        queueRepository.save(queue);

        return request;
    }

    @Override
    public Request getCurrentRequest() {
        Queue queue = getOrCreateQueue();
        Request currentRequest = queue.getCurrentRequest();
        if (currentRequest == null && !queue.getRequests().isEmpty()) {
            currentRequest = queue.getRequests().get(0);
            queue.setCurrentRequest(currentRequest);
            queue.getRequests().remove(0);
            queueRepository.save(queue);
        }
        return currentRequest;
    }

    @Override
    public void markCurrentRequestDone() {
        Queue queue = getOrCreateQueue();
        Request currentRequest = queue.getCurrentRequest();
        if (currentRequest != null) {
            currentRequest.setRequestType(RequestType.DONE);
            currentRequest.setAppointmentDateTime(LocalDateTime.now().toString());
            currentRequest.setWaitingTime(String.valueOf(Duration.between(LocalDateTime.parse(currentRequest.getAppointmentTime()),
                                            LocalDateTime.parse(currentRequest.getAppointmentDateTime()))));
            queue.setCurrentRequest(null);
            queueRepository.save(queue);
        }
    }

    @Override
    public List<Request> getRequests() {
        return new ArrayList<>(queue.values());
    }

    @Override
    public void removeRequest(Request request) {
        Queue queue = getOrCreateQueue();
        if (queue.getRequests().remove(request)) {
            queueRepository.save(queue);
        } else if (queue.getCurrentRequest() != null && queue.getCurrentRequest().equals(request)) {
            queue.setCurrentRequest(null);
            queueRepository.save(queue);
        }
    }

    @Override
    public void recalculateEstimatedTime() {
        Queue queue = getOrCreateQueue();
        int queueSize = queue.getRequests().size();
        long waitingTimeInSeconds = queueSize * averageWaitingTimeInMinutes;
        for (Request request : queue.getRequests()) {
            request.setEstimatedTime(String.valueOf(Duration.ofSeconds(waitingTimeInSeconds)));
            waitingTimeInSeconds -= averageWaitingTimeInMinutes;
        }
        queueRepository.save(queue);
    }

    @Override
    public void removeFromQueue(Client client) {
        Queue queue = getOrCreateQueue();
        List<Request> requestsToRemove = queue.getRequests()
                .stream()
                .filter(request -> request.getClient().equals(client))
                .collect(Collectors.toList());
        if (requestsToRemove.size() > 0) {
            queue.getRequests().removeAll(requestsToRemove);
            if (queue.getCurrentRequest() != null && queue.getCurrentRequest().getClient().equals(client)) {
                queue.setCurrentRequest(null);
            }
            queueRepository.save(queue);
        }
    }

    @Override
    public List<Client> getClientQueue() {
        return new ArrayList<>(queue.keySet());
    }

    private Queue getOrCreateQueue() { //ищем очередь в репозитории
        Optional<Queue> optionalQueue = queueRepository.findTopByOrderByIdDesc();

        if (optionalQueue.isPresent()) {
            Queue queue = optionalQueue.get();
            if (queue.getCurrentRequest() == null) { //если нет текущего Request в нашей очереди вернем ее
                return queue;
            }
        }

        Queue newQueue = new Queue(); // иначе создадим новую очередь
        newQueue.setNextQueueNumber(1);
        newQueue.setPriorityClient(false);
        return queueRepository.save(newQueue);
    }
}
