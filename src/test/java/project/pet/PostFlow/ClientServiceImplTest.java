package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import project.pet.PostFlow.enums.CRUDStatus;
import project.pet.PostFlow.model.dto.ClientDTO;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.entity.Department;
import project.pet.PostFlow.model.repository.ClientRepository;
import project.pet.PostFlow.services.service.ClientService;
import project.pet.PostFlow.services.serviceImpl.ClientServiceImpl;

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

        ClientDTO clientDTO = clientService.getClientById(id);

        assertEquals(firstName, clientDTO.getFirstName());
        assertEquals(lastName, clientDTO.getLastName());
        assertEquals("Голикова", clientDTO.getDepartment().getName());
    }

    @Test
    public void testDeleteClientById() {
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("Вася");
        client.setLastName("Пупкин");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.deleteClientById(1L);

        verify(clientRepository, times(1)).findById(1L);

        assertEquals(CRUDStatus.DELETED, client.getStatus());
    }

    @Test
    public void testAddClient() {
        Client clientToSave = new Client();
        clientToSave.setFirstName("Вася");
        clientToSave.setLastName("Пупкин");
        Department department = new Department();
        department.setName("Голикова");
        clientToSave.setDepartment(department);

        when(clientRepository.save(clientToSave)).thenReturn(clientToSave);

        Client savedClient = clientService.addClient(clientToSave);

        assertEquals("Вася", savedClient.getFirstName());
        assertEquals("Пупкин", savedClient.getLastName());
        assertEquals("Голикова", savedClient.getDepartment().getName());

        verify(clientRepository, times(1)).save(clientToSave);
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

        ClientDTO updatedClientDTO = new ClientDTO();
        Department department1 = new Department();
        department.setName("Невский");
        updatedClientDTO.setId(1L);
        updatedClientDTO.setFirstName("Вася");
        updatedClientDTO.setLastName("Пупкин");
        updatedClientDTO.setDepartment(department1);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(clientToUpdate));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);

        ClientDTO actualClientDTO = clientService.updateClient(updatedClientDTO);

        verify(clientRepository, times(1)).findById(1L);

        verify(clientRepository, times(1)).save(eq(clientToUpdate));

        assertEquals(updatedClientDTO.getId(), actualClientDTO.getId());
        assertEquals(updatedClientDTO.getFirstName(), actualClientDTO.getFirstName());
        assertEquals(updatedClientDTO.getLastName(), actualClientDTO.getLastName());
        assertEquals(updatedClientDTO.getDepartment().getName(), actualClientDTO.getDepartment().getName());
    }

    @Test
    public void testCreateClient() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setUserName("testUser");
        clientDTO.setPassword("testPassword123");
        clientDTO.setEmail("test@test.com");

        when(clientRepository.findByUserName(eq(clientDTO.getUserName()))).thenReturn(Optional.empty());
        when(clientRepository.findByEmail(eq(clientDTO.getEmail()))).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(new Client());

        ClientDTO result = clientService.createClient(clientDTO);

        assertNotNull(result);
        verify(clientRepository, times(1)).findByUserName(eq(clientDTO.getUserName()));
        verify(clientRepository, times(1)).findByEmail(eq(clientDTO.getEmail()));
        verify(clientRepository, times(1)).save(any(Client.class));
    }
}

