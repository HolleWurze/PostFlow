package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import project.pet.PostFlow.Enum.ClientPriority;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTO;
import project.pet.PostFlow.Model.DTO.RequestDTO;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Queue;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.QueueRepository;
import project.pet.PostFlow.Services.Service.RequestService;
import project.pet.PostFlow.Services.ServiceImpl.QueueServiceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QueueServiceImplTest {

    private Map<Client, Request> queue = new LinkedHashMap<>();
    @Mock
    private QueueRepository queueRepository;
    @Mock
    private RequestService requestService;

    private final int averageWaitingTimeInMinutes = 10;

    @Spy
    private ModelMapper modelMapper;
    @Spy
    private ObjectMapper mapper;

    @InjectMocks
    private QueueServiceImpl queueServiceImpl;

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

    @Test
    public void testGetOrCreateQueue_notEmpty() {
        Request request = new Request();
        request.setWaitingTime("10");
        Queue newQueue = new Queue();
        newQueue.setRequests(Collections.singletonList(request));

        when(queueRepository.findAll()).thenReturn(Collections.singletonList(newQueue));
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
        assertEquals(request.getWaitingTime(),result.getRequests().get(0).getWaitingTime());
    }

    @Test
    public void testGetCurrentRequest_currentRequestNotNull() {
        Queue queue = new Queue();
        Request currentRequest = new Request();
        currentRequest.setId(1L);
        currentRequest.setClient(new Client());
        List<Request> requests = new ArrayList<>();
        requests.add(currentRequest);
        queue.setRequests(requests);
        queue.setCurrentRequest(currentRequest);

        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));

        RequestDTO result = queueServiceImpl.getCurrentRequest();

        assertNotNull(result);
        assertEquals(currentRequest.getId(), result.getId());
        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
    }


    @Test
    public void testGetCurrentRequest_currentRequestIsNull() {
        Queue queue = new Queue();
        queue.setId(1L);

        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));

        RequestDTO result = queueServiceImpl.getCurrentRequest();

        assertNull(result);
        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
        verify(queueRepository, never()).save(any(Queue.class));
    }

    @Test
    public void testMarkCurrentRequestDone() {
        Queue queue = new Queue();
        Request currentRequest = new Request();
        currentRequest.setRequestType(RequestType.GET_PARCEL);
        LocalDateTime appointmentTime = LocalDateTime.now();
        currentRequest.setAppointmentTime(appointmentTime.toString());
        currentRequest.setWaitingTime("0");
        queue.setCurrentRequest(currentRequest);

        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));
        when(queueRepository.save(any(Queue.class))).thenReturn(queue);

        queueServiceImpl.markCurrentRequestDone();

        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
        verify(queueRepository, times(1)).save(any(Queue.class));

        assertNull(queue.getCurrentRequest());
        assertEquals(RequestType.DONE, currentRequest.getRequestType());
        assertEquals(Duration.ofMinutes(0), Duration.between(appointmentTime, LocalDateTime.parse(currentRequest.getAppointmentTime())));
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

        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));
        when(queueRepository.save(any(Queue.class))).thenReturn(queue);

        queueServiceImpl.removeFromQueue(client);

        verify(queueRepository, times(1)).save(queue);
        assertEquals(2, queue.getRequests().size());
        assertFalse(queue.getRequests().contains(request2));
        assertNull(queue.getCurrentRequest());
    }

    @Test
    public void testRecalculateEstimatedTime_emptyQueue() {
        Queue emptyQueue = new Queue();
        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(emptyQueue));

        queueServiceImpl.recalculateEstimatedTime(new Client());

        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
        verify(queueRepository, times(1)).save(emptyQueue);
    }

//    @Test
//    public void testRecalculateEstimatedTime_multipleRequestsForSameClient() {
//        Client client = new Client();
//        Request request1 = new Request(client, RequestType.GET_PARCEL, null);
//        Request request2 = new Request(client, RequestType.GET_PARCEL, null);
//        List<Request> requests = Arrays.asList(request1, request2);
//        Queue queue = new Queue();
//        queue.setRequests(requests);
//        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));
//
//        queueServiceImpl.recalculateEstimatedTime(client);
//
//        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
//        verify(queueRepository, times(1)).save(queue);
//        assertEquals(Duration.ZERO, Duration.parse(request1.getEstimatedTime()));
//        assertEquals(String.valueOf(Duration.ofMinutes(-averageWaitingTimeInMinutes)), request2.getEstimatedTime(), "Incorrect estimated time");
//    }
//
//    @Test
//    public void testRecalculateEstimatedTime_multipleRequestsForDifferentClients() {
//        Client client1 = new Client();
//        Client client2 = new Client();
//        Request request1 = new Request(client1, RequestType.GET_PARCEL, null);
//        Request request2 = new Request(client2, RequestType.GET_PARCEL, null);
//        List<Request> requests = Arrays.asList(request1, request2);
//        Queue queue = new Queue();
//        queue.setRequests(requests);
//        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));
//
//        queueServiceImpl.recalculateEstimatedTime(client1);
//
//        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
//        verify(queueRepository, times(1)).save(queue);
//        assertEquals(Duration.ZERO, Duration.parse(request1.getEstimatedTime()));
//        assertNull(request2.getEstimatedTime());
//    }
}
