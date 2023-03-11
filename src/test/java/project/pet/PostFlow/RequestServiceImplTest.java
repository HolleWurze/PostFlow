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
import project.pet.PostFlow.Model.Entity.*;
import project.pet.PostFlow.Model.Repository.ClientRepository;
import project.pet.PostFlow.Model.Repository.RequestRepository;
import project.pet.PostFlow.Services.Service.RequestService;
import project.pet.PostFlow.Services.ServiceImpl.RequestServiceImpl;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestServiceImplTest {
    @Mock
    private  RequestRepository requestRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RequestService requestService;
    @Spy
    private ModelMapper modelMapper;
    @Spy
    private  ObjectMapper mapper;
    @InjectMocks
    RequestServiceImpl requestServiceImpl;


    @Test
    public void testDeleteRequestsByDepartmentId() {
        Long departmentId = 1L;

        boolean result = requestServiceImpl.deleteRequestsByDepartmentId(departmentId);

        verify(requestRepository, times(1)).deleteByDepartmentId(departmentId);
        assertTrue(result);
    }

    @Test
    public void deleteRequestById_requestExists_requestDeleted() {
        Long requestId = 1L;
        Request request = new Request();
        request.setId(requestId);
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));

        boolean result = requestServiceImpl.deleteRequestById(requestId);

        verify(requestRepository, times(1)).deleteById(requestId);
        assertTrue(result);
    }

    @Test
    public void deleteRequestById_requestDoesNotExist_falseReturned() {
        Long requestId = 1L;
        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        boolean result = requestServiceImpl.deleteRequestById(requestId);

        verify(requestRepository, times(0)).deleteById(requestId);
        assertFalse(result);
    }

    @Test
    public void testGetAllRequests() {
        Request request1 = new Request();
        Request request2 = new Request();
        Request request3 = new Request();

        List<Request> requests = Arrays.asList(request1, request2, request3);
        when(requestRepository.findAll()).thenReturn(requests);

        List<Request> result = requestServiceImpl.getAllRequests();

        assertEquals(requests.size(), result.size());
        assertTrue(result.contains(request1));
        assertTrue(result.contains(request2));
        assertTrue(result.contains(request3));
    }

    @Test
    public void testDeleteRequestById() {
        Long requestId = 1L;

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(new Request()));

        boolean result = requestServiceImpl.deleteRequestById(requestId);

        verify(requestRepository, times(1)).findById(requestId);
        verify(requestRepository, times(1)).deleteById(requestId);

        assertTrue(result);
    }


    @Test
    public void testCreateRequest() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("Вася");
        clientDTO.setLastName("Пупкин");
        clientDTO.setClientPriority(ClientPriority.REGULAR);

        RequestType requestType = RequestType.GET_PARCEL;
        String appointmentTime = "2022-01-01 00:00:00";

        Request request = new Request();
        when(clientRepository.save(any())).thenReturn(new Client());
        when(requestRepository.save(any())).thenReturn(request);

        RequestDTO createdRequest = requestServiceImpl.createRequest(clientDTO, requestType, appointmentTime);

        assertNotNull(createdRequest);
    }

    @Test
    public void testUpdateRequest() {
        Request existingRequest = new Request();
        existingRequest.setId(1L);
        existingRequest.setDepartment(new Department());
        existingRequest.setParcel(new Parcel());
        existingRequest.setWaitingTime("5 минут");
        existingRequest.setEstimatedTime("10 минут");
        existingRequest.setRequestType(RequestType.IN_PROGRESS);

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setId(existingRequest.getId());
        requestDTO.setDepartment(new Department());
        requestDTO.setParcel(new Parcel());
        requestDTO.setWaitingTime("10 минут");
        requestDTO.setEstimatedTime("15 минут");
        requestDTO.setRequestType(RequestType.DONE);

        when(requestRepository.findById(existingRequest.getId())).thenReturn(Optional.of(existingRequest));
        when(requestRepository.save(existingRequest)).thenReturn(existingRequest);

        RequestDTO updatedRequestDTO = requestServiceImpl.updateRequest(requestDTO);
        assertNotNull(updatedRequestDTO);
        assertEquals(requestDTO.getWaitingTime(), existingRequest.getWaitingTime());
        assertEquals(requestDTO.getEstimatedTime(), existingRequest.getEstimatedTime());
        assertEquals(requestDTO.getRequestType(), existingRequest.getRequestType());
    }
}
