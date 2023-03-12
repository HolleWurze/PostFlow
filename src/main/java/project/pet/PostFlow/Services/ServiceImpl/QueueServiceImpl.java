package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.Enum.ClientPriority;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTO;
import project.pet.PostFlow.Model.DTO.RequestDTO;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Queue;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.QueueRepository;
import project.pet.PostFlow.Services.Service.QueueService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final Map<Client, Request> queueMap;
    private final QueueRepository queueRepository;
    private final int averageWaitingTimeInMinutes = 10;

   // private final ModelMapper modelMapper;

    private final ObjectMapper mapper;

    public Queue getOrCreateQueue() {
        Optional<Queue> optionalQueue = queueRepository.findTopByOrderByIdDesc();
        if (optionalQueue.isPresent()) {
            Queue queue = optionalQueue.get();
            if (queue.getRequests() == null) {
                queue.setRequests(new ArrayList<>());
            }
            return queue;
        }
        Queue newQueue = new Queue();
        newQueue.setNextQueueNumber(1);
        newQueue.setPriorityClient(false);
        newQueue.setRequests(new ArrayList<>()); // Создаем пустой список, если очередь еще не была создана
        return queueRepository.save(newQueue);
    }

    @Override
    public RequestDTO addRequest(ClientDTO clientDTO, RequestType requestType, String appointmentTime) {
        Queue queue = getOrCreateQueue();
        Client client = mapper.convertValue(clientDTO, Client.class);
        Request request = new Request(client, requestType, appointmentTime);

        if (clientDTO.getClientPriority() == ClientPriority.PRIORITY) {
            // Если у клиента приоритетный статус, то мы его сразу обслуживаем
            request.setRequestType(RequestType.DONE);
            request.setAppointmentTime(LocalDateTime.now().toString());
            request.setWaitingTime(String.valueOf(Duration.ZERO));
            queue.setCurrentRequest(request);
        } else {
            List<Request> requests = queue.getRequests();
            if (requests == null) {
                requests = new ArrayList<>();
            }
            requests = requests.stream()
                    .filter(r -> r.getClient().equals(client))
                    .collect(Collectors.toList());

            if (!requests.isEmpty()) {
                Request lastRequest = requests.get(requests.size() - 1);
                recalculateEstimatedTime(client);
                Optional<String> appointmentDateTime = Optional.ofNullable(lastRequest.getAppointmentTime());
                long waitingTimeInSeconds = appointmentDateTime
                        .map(appointment -> Duration.between(LocalDateTime.parse(appointment), LocalDateTime.now()).getSeconds())
                        .orElse(0L);
                request.setEstimatedTime(String.valueOf(Duration.ofSeconds(waitingTimeInSeconds)));
                requests.add(request);
            } else {
                requests.add(request);
                if (queue.getNextQueueNumber() == null) {
                    queue.setNextQueueNumber(1);
                } else {
                    queue.setNextQueueNumber(queue.getNextQueueNumber() + 1);
                }
            }
            queue.setRequests(requests);
        }
        queueRepository.save(queue);
        return mapper.convertValue(request, RequestDTO.class);
    }

    @Override
    public RequestDTO getCurrentRequest() {
        Queue queue = getOrCreateQueue();
        if (queue == null || queue.getRequests() == null || queue.getRequests().isEmpty()) {
            return null; // no requests in the queue
        }
        Request currentRequest = queue.getCurrentRequest();
        if (currentRequest == null && !queue.getRequests().isEmpty()) {
            currentRequest = queue.getRequests().get(0);
            queue.setCurrentRequest(currentRequest);
            queue.getRequests().remove(0);
            queueRepository.save(queue);
        }
        return mapper.convertValue(currentRequest, RequestDTO.class);
    }

    @Override
    public void markCurrentRequestDone() {
        Queue queue = getOrCreateQueue();
        Request currentRequest = queue.getCurrentRequest();
        if (currentRequest != null) {
            currentRequest.setRequestType(RequestType.DONE);
            LocalDateTime appointmentTime = LocalDateTime.parse(currentRequest.getAppointmentTime());
            currentRequest.setAppointmentTime(String.valueOf(LocalDateTime.now()));
            currentRequest.setWaitingTime(String.valueOf(Duration.between(appointmentTime, LocalDateTime
                    .parse(currentRequest.getAppointmentTime())).toMinutes()));
            queue.getRequests().remove(currentRequest);
            queue.setCurrentRequest(null);
            queueRepository.save(queue);
        }
    }

    @Override
    public List<Request> getRequests() {
        return new ArrayList<>(queueMap.values());
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
    public void recalculateEstimatedTime(Client client) {
        Queue queue = getOrCreateQueue();
        List<Request> requests = queue.getRequests()
                .stream()
                .filter(request -> request.getClient().equals(client))
                .collect(Collectors.toList());
        int queueSize = requests.size();
        long waitingTimeInSeconds = queueSize * averageWaitingTimeInMinutes;
        for (Request request : requests) {
            request.setEstimatedTime(
                    String.valueOf(Duration.ofSeconds(waitingTimeInSeconds)));
            waitingTimeInSeconds -= averageWaitingTimeInMinutes;
        }
        queueRepository.save(queue);
    }

    @Override
    public void removeFromQueue(Client client) {
        Queue queue = getOrCreateQueue();
        if (queue.getRequests() == null || queue.getRequests().isEmpty()) {
            return; // no requests in the queue
        }
        List<Request> requestsToRemove = queue.getRequests()
                .stream()
                .filter(request -> request.getClient().equals(client))
                .collect(Collectors.toList());
        if (requestsToRemove.isEmpty()) {
            return; // no requests found for this client
        }
        queue.getRequests().removeAll(requestsToRemove);
        if (queue.getCurrentRequest() != null && queue.getCurrentRequest().getClient().equals(client)) {
            queue.setCurrentRequest(null);
        }
        queueRepository.save(queue);
    }

    @Override
    public List<Client> getClientQueue() {
        return new ArrayList<>(queueMap.keySet());
    }
}
