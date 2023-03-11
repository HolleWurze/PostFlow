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

    private final Map<Client, Request> queue = new LinkedHashMap<>();
    private final QueueRepository queueRepository;
    private final int averageWaitingTimeInMinutes = 10;

    private final ModelMapper modelMapper;

    private final ObjectMapper mapper;

    @Override
    public RequestDTO addRequest(ClientDTO clientDTO, RequestType requestType, String appointmentTime) {
        Queue queue = getOrCreateQueue();
        Client client = modelMapper.map(clientDTO, Client.class);
        Request request = new Request(client, requestType, appointmentTime);

        if (clientDTO.getClientPriority() == ClientPriority.PRIORITY) {
            // Если у клиента приоритетный статус то мы его сразу обслуживаем
            request.setRequestType(RequestType.DONE);
            request.setAppointmentTime(LocalDateTime.now().toString());
            request.setWaitingTime(String.valueOf(Duration.ZERO));
            queue.setCurrentRequest(request);
        } else {
            List<Request> requests = queue.getRequests().stream()
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
                queue.getRequests().add(request);
            } else {
                queue.getRequests().add(request);
                queue.setNextQueueNumber(queue.getNextQueueNumber() + 1);
            }
        }

        queueRepository.save(queue);
        return mapper.convertValue(request, RequestDTO.class);
    }

    @Override
    public RequestDTO getCurrentRequest() {
        Queue queue = getOrCreateQueue();
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
    public void recalculateEstimatedTime(Client client) {
        Queue queue = getOrCreateQueue();
        List<Request> requests = queue.getRequests().stream()
                .filter(request -> request.getClient().equals(client))
                .collect(Collectors.toList());
        int queueSize = requests.size();
        long waitingTimeInSeconds = queueSize * averageWaitingTimeInMinutes;
        for (Request request : requests) {
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

    public Queue getOrCreateQueue() {
        if (queueRepository != null) {
            List<Queue> queues = queueRepository.findAll();
            if (!queues.isEmpty()) {
                return queues.get(queues.size() - 1);
            }
        }
        Queue newQueue = new Queue();
        newQueue.setNextQueueNumber(1);
        newQueue.setPriorityClient(false);
        return queueRepository.save(newQueue);
    }
}
