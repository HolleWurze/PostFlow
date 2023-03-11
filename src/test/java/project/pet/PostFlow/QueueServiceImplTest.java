package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import project.pet.PostFlow.Enum.ClientPriority;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTO;
import project.pet.PostFlow.Model.DTO.RequestDTO;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Queue;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.ClientRepository;
import project.pet.PostFlow.Model.Repository.QueueRepository;
import project.pet.PostFlow.Model.Repository.RequestRepository;
import project.pet.PostFlow.Services.Service.ClientService;
import project.pet.PostFlow.Services.Service.RequestService;
import project.pet.PostFlow.Services.ServiceImpl.QueueServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueueServiceImplTest {

    private Map<Client, Request> queue = new LinkedHashMap<>();
    @Mock
    private QueueRepository queueRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private RequestService requestService;
    @Mock
    private ClientService clientService;
    @Spy
    private ModelMapper modelMapper;
    @Spy
    private ObjectMapper mapper;

    @InjectMocks
    private QueueServiceImpl queueService;

    @Before
    public void setup() {
        queueService = new QueueServiceImpl(queueRepository, modelMapper, mapper);
    }

    @Test
    public void testAddRequest() {
        // Создаем тестовые данные
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");
        clientDTO.setClientPriority(ClientPriority.REGULAR);
        RequestType requestType = RequestType.IN_PROGRESS;
        String appointmentTime = LocalDateTime.now().toString();

        // Настройка объекта-заглушки для возврата не-`null` значения
        Queue queue = new Queue();
        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));

        // Вызов метода, который будет тестироваться
        RequestDTO result = queueService.addRequest(clientDTO, requestType, appointmentTime);

        // Проверка результата
        assertNotNull(result);

        // Проверка того, что объект-заглушка был вызван
        verify(queueRepository, times(1)).findTopByOrderByIdDesc();
        verify(queueRepository, times(1)).save(any(Queue.class));
    }

    @Test
    public void testAddRegularRequest() {
        // Создаем клиента с обычным статусом
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("Вася");
        clientDTO.setLastName("Пупкин");
        clientDTO.setClientPriority(ClientPriority.REGULAR);

        // Создаем mock для getOrCreateQueue()
        Queue queue = new Queue();
        when(queueRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(queue));
        when(queueRepository.save(any(Queue.class))).thenReturn(queue);

        // Создаем mock для возвращаемого значения от requestService.createRequest
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRequestType(RequestType.NEW);
        requestDTO.setAppointmentTime(LocalDateTime.now().toString());
        when(requestService.createRequest(clientDTO, RequestType.NEW, LocalDateTime.now().toString())).thenReturn(requestDTO);

        // Выполняем метод addRequest()
        RequestDTO result = queueService.addRequest(clientDTO, RequestType.NEW, LocalDateTime.now().toString());

        // Проверяем, что возвращается правильный объект RequestDTO
        assertNotNull(result);
        assertEquals(requestDTO, result);

        // Проверяем, что очередь сохраняется
        verify(queueRepository, times(1)).save(any(Queue.class));
    }

    @Test
    public void testGetClientQueue() {
        // Создаем клиентов и запросы
        Client client1 = new Client();
        Request request1 = new Request(client1, RequestType.GET_PARCEL, LocalDateTime.now().toString());

        Client client2 = new Client();
        Request request2 = new Request(client2, RequestType.GET_PARCEL, LocalDateTime.now().toString());

        Client client3 = new Client();
        Request request3 = new Request(client3, RequestType.SEND_PARCEL, LocalDateTime.now().toString());

        // Добавляем запросы в очередь
        QueueServiceImpl queueService = new QueueServiceImpl(queueRepository, new ModelMapper(), new ObjectMapper());
        queueService.addRequest(modelMapper.map(client1, ClientDTO.class), RequestType.GET_PARCEL, LocalDateTime.now().toString());
        queueService.addRequest(modelMapper.map(client2, ClientDTO.class), RequestType.GET_PARCEL, LocalDateTime.now().toString());
        queueService.addRequest(modelMapper.map(client3, ClientDTO.class), RequestType.SEND_PARCEL, LocalDateTime.now().toString());

        // Получаем список клиентов в очереди
        List<Client> clientQueue = queueService.getClientQueue();

        // Проверяем, что список содержит всех добавленных клиентов
        assertEquals(3, clientQueue.size());
        assertTrue(clientQueue.contains(client1));
        assertTrue(clientQueue.contains(client2));
        assertTrue(clientQueue.contains(client3));
    }

    @Test
    public void testRemoveFromQueue() {
        // Создаем клиента и добавляем в очередь
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("Вася");
        clientDTO.setLastName("Пупкин");
        RequestDTO request = queueService.addRequest(clientDTO, RequestType.GET_PARCEL, LocalDateTime.now().toString());

        // Вызываем метод removeFromQueue()
        queueService.removeFromQueue(modelMapper.map(clientDTO, Client.class));

        // Проверяем, что клиент удален из очереди и очередь сохраняется
        verify(queueRepository, times(1)).save(any(Queue.class));
        assertEquals(Collections.emptyList(), queueService.getRequests());
    }





}
