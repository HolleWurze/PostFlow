package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import project.pet.PostFlow.enums.ClientPriority;
import project.pet.PostFlow.enums.RequestType;
import project.pet.PostFlow.model.dto.ClientDTO;
import project.pet.PostFlow.model.dto.RequestDTO;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.entity.Queue;
import project.pet.PostFlow.model.entity.Request;
import project.pet.PostFlow.model.repository.QueueRepository;
import project.pet.PostFlow.services.service.RequestService;
import project.pet.PostFlow.services.serviceImpl.QueueServiceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QueueServiceImplTest {
    @Mock
    private QueueRepository queueRepository;
    private static final int averageWaitingTimeInMinutes = 5*60;
    @Spy
    private ObjectMapper mapper;
    @InjectMocks
    private QueueServiceImpl queueServiceImpl;

    private Client client;
    private Queue queueTest;
    private List<Request> requests;

    @Before
    public void setUp() {
        client = new Client();
        queueTest = new Queue();
        requests = createTestRequests();
        queueTest.setRequests(requests);
    }

    private List<Request> createTestRequests() {
        List<Request> requests = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Request request = new Request();
            request.setClient(client);
            requests.add(request);
        }
        return requests;
    }

    @Test
    public void recalculateEstimatedTime_success() {
        when(queueRepository.save(any(Queue.class))).thenReturn(queueTest);
        when(queueServiceImpl.getOrCreateQueue()).thenReturn(queueTest);

        queueServiceImpl.recalculateEstimatedTime(client);

        List<Request> filteredRequests = requests.stream()
                .filter(request -> request.getClient().equals(client))
                .collect(Collectors.toList());

        long waitingTimeInSeconds = filteredRequests.size() * averageWaitingTimeInMinutes;
        for (Request request : filteredRequests) {
            assertEquals(
                    String.valueOf(Duration.ofSeconds(waitingTimeInSeconds)),
                    request.getEstimatedTime());
            waitingTimeInSeconds -= averageWaitingTimeInMinutes;
        }

        verify(queueRepository).save(queueTest);
    }

    @Test
    public void testAddRequest() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");
        clientDTO.setClientPriority(ClientPriority.REGULAR);
        RequestType requestType = RequestType.IN_PROGRESS;
        String appointmentTime = LocalDateTime.now().toString();

        Queue queue = new Queue();
        queue.setRequests(new ArrayList<>());

        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));
        when(queueRepository.save(any(Queue.class))).thenReturn(queue);

        RequestDTO result = queueServiceImpl.addRequest(clientDTO, requestType, appointmentTime);

        assertNotNull(result);
        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
        verify(queueRepository, times(1)).save(any(Queue.class));
    }

//    @Test
//    public void testGetCurrentRequest_currentRequestNotNull() {
//        Queue queue = new Queue();
//        Request currentRequest = new Request();
//        currentRequest.setId(1L);
//        currentRequest.setClient(new Client());
//        List<Request> requests = new ArrayList<>();
//        requests.add(currentRequest);
//        queue.setRequests(requests);
//        queue.setCurrentRequest(currentRequest);
//
//        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));
//
//        RequestDTO result = queueServiceImpl.getCurrentRequest();
//
//        assertNotNull(result);
//        assertEquals(currentRequest.getId(), result.getId());
//        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
//    }
//
//
//    @Test
//    public void testGetCurrentRequest_currentRequestIsNull() {
//        Queue queue = new Queue();
//        queue.setId(1L);
//
//        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));
//
//        RequestDTO result = queueServiceImpl.getCurrentRequest();
//
//        assertNull(result);
//        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
//        verify(queueRepository, never()).save(any(Queue.class));
//    }
//
//    @Test
//    public void testRecalculateEstimatedTime_emptyQueue() {
//        Queue emptyQueue = new Queue();
//        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(emptyQueue));
//
//        queueServiceImpl.recalculateEstimatedTime(new Client());
//
//        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
//        verify(queueRepository, times(1)).save(emptyQueue);
//    }

    @Test
    public void testGetOrCreateQueue_notEmpty() {
        Request request = new Request();
        request.setWaitingTime("10");
        Queue newQueue = new Queue();
        newQueue.setRequests(Collections.singletonList(request));

        when(queueRepository.save(any(Queue.class))).thenReturn(newQueue);

        Queue result = queueServiceImpl.getOrCreateQueue();
        assertEquals(request.getWaitingTime(), result.getRequests().get(0).getWaitingTime());
    }

    @Test
    public void testGetOrCreateQueue_emptyRepo() {
        Request request = new Request();
        request.setWaitingTime("10");
        Queue newQueue = new Queue();
        newQueue.setRequests(Collections.singletonList(request));

        when(queueRepository.save(any(Queue.class))).thenReturn(newQueue);

        Queue result = queueServiceImpl.getOrCreateQueue();
        assertEquals(request.getWaitingTime(), result.getRequests().get(0).getWaitingTime());
    }

    @Test
    public void testRemoveFromQueue() {
        Client client = new Client();
        Queue queue = new Queue();
        List<Request> requests = new ArrayList<>();
        Request request1 = new Request(new Client(), RequestType.GET_PARCEL, "2022-04-01T16:00:00");
        Request request2 = new Request(client, RequestType.GET_PARCEL, "2022-04-01T16:10:00");
        Request request3 = new Request(new Client(), RequestType.SEND_PARCEL, "2022-04-01T16:20:00");
        requests.add(request1);
        requests.add(request2);
        requests.add(request3);
        queue.setRequests(requests);

        when(queueRepository.save(any(Queue.class))).thenReturn(queue);

        queueServiceImpl.removeFromQueue(client);

        verify(queueRepository, times(1)).save(queue);
        assertEquals(2, queue.getRequests().size());
        assertFalse(queue.getRequests().contains(request2));
        assertNull(queue.getCurrentRequest());
    }
}
