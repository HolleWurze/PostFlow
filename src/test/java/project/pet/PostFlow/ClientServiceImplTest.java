package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import project.pet.PostFlow.Enum.CRUDStatus;
import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Repository.ClientRepository;
import project.pet.PostFlow.Services.Service.ClientService;
import project.pet.PostFlow.Services.ServiceImpl.ClientServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplTest {
    @Spy
    private ObjectMapper mapper;
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void testGetAllClients() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        clients.add(new Client());
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.getAllClients();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetClientById() {
        Long id = 1L;
        String firstName = "Вася";
        String lastName = "Пупкин";
        Department department = new Department();
        department.setName("Голикова");
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setDepartment(department);

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        ClientDTORequest clientDTO = clientService.getClientById(id);

        assertEquals(firstName, clientDTO.getFirstName());
        assertEquals(lastName, clientDTO.getLastName());
        assertEquals("Голикова", clientDTO.getDepartment().getName());
    }

    @Test
    public void testAddClient() {
        Client clientToSave = new Client();
        clientToSave.setFirstName("Вася");
        clientToSave.setLastName("Пупкин");
        Department department = new Department();
        department.setName("Голикова");
        clientToSave.setDepartment(department);

        Mockito.when(clientRepository.save(clientToSave)).thenReturn(clientToSave);

        Client savedClient = clientService.addClient(clientToSave);

        assertEquals("Вася", savedClient.getFirstName());
        assertEquals("Пупкин", savedClient.getLastName());
        assertEquals("Голикова", savedClient.getDepartment().getName());

        Mockito.verify(clientRepository, Mockito.times(1)).save(clientToSave);
    }

    @Test
    public void testSaveClient() {
        Client client = new Client();
        Department department = new Department();
        department.setName("Голикова");
        client.setFirstName("Вася");
        client.setLastName("Пупкин");
        client.setDepartment(department);

        Mockito.when(clientRepository.save(client)).thenReturn(client);

        ObjectMapper objectMapper = new ObjectMapper();

        ClientService clientService = new ClientServiceImpl(clientRepository, objectMapper);

        Client savedClient = clientService.saveClient(client);

        Mockito.verify(clientRepository, Mockito.times(1)).save(client);

        assertEquals(client, savedClient);
    }

    @Test
    public void testUpdateClient() {
        Client clientToUpdate = new Client();
        Department department = new Department();
        department.setName("Голикова");
        clientToUpdate.setId(1L);
        clientToUpdate.setFirstName("Вася");
        clientToUpdate.setLastName("Пупкин");
        clientToUpdate.setDepartment(department);

        ClientDTORequest updatedClientDTO = new ClientDTORequest();
        Department department1 = new Department();
        department.setName("Невский");
        updatedClientDTO.setId(1L);
        updatedClientDTO.setFirstName("Вася");
        updatedClientDTO.setLastName("Пупкин");
        updatedClientDTO.setDepartment(department1);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(clientToUpdate));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);

        ClientDTORequest actualClientDTO = clientService.updateClient(updatedClientDTO);

        verify(clientRepository, times(1)).findById(1L);

        verify(clientRepository, times(1)).save(eq(clientToUpdate));

        assertEquals(updatedClientDTO.getId(), actualClientDTO.getId());
        assertEquals(updatedClientDTO.getFirstName(), actualClientDTO.getFirstName());
        assertEquals(updatedClientDTO.getLastName(), actualClientDTO.getLastName());
        assertEquals(updatedClientDTO.getDepartment().getName(), actualClientDTO.getDepartment().getName());
    }

    @Test
    public void testCreateClient() {
        ClientDTORequest clientDTORequest = new ClientDTORequest();
        clientDTORequest.setFirstName("Вася");
        clientDTORequest.setLastName("Пупкин");
        Department department = new Department();
        department.setName("Невский");
        clientDTORequest.setDepartment(department);

        when(clientRepository.findById(clientDTORequest.getId())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClientDTORequest createdClientDTO = clientService.createClient(clientDTORequest);

        assertEquals(clientDTORequest.getFirstName(), createdClientDTO.getFirstName());
        assertEquals(clientDTORequest.getLastName(), createdClientDTO.getLastName());
        assertEquals(clientDTORequest.getDepartment().getName(), createdClientDTO.getDepartment().getName());

        verify(clientRepository, times(1)).findById(clientDTORequest.getId());

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void testDeleteClientById() {
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("Вася");
        client.setLastName("Пупкин");

        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.deleteClientById(1L);

        Mockito.verify(clientRepository, Mockito.times(1)).findById(1L);

        Mockito.verify(clientRepository, Mockito.times(1)).save(client);

        assertEquals(CRUDStatus.DELETED, client.getStatus());
    }
}

