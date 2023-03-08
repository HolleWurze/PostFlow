package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.CustomException.AlreadyExistsException;
import project.pet.PostFlow.CustomException.NotFoundException;
import project.pet.PostFlow.Enum.CRUDStatus;
import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Repository.ClientRepository;
import project.pet.PostFlow.Services.Service.ClientService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    private final ObjectMapper mapper;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public ClientDTORequest getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        return mapper.convertValue(client, ClientDTORequest.class);
    }

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public ClientDTORequest updateClient(ClientDTORequest clientDTORequest) {
        Client existingClient = clientRepository.findById(clientDTORequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Клиент с таким ID не найден " + clientDTORequest.getId()));
        existingClient.setFirstName(clientDTORequest.getFirstName());
        existingClient.setLastName(clientDTORequest.getLastName());
        existingClient.setDepartment(clientDTORequest.getDepartment());

        return mapper.convertValue(clientRepository.save(existingClient), ClientDTORequest.class);
    }

    @Override
    public ClientDTORequest createClient(ClientDTORequest clientDTORequest) {
        clientRepository.findById(clientDTORequest.getId()).ifPresent(
                c -> {
                    throw new AlreadyExistsException("Клиент с таким ID уже существует ", HttpStatus.BAD_REQUEST);
                }
        );

        Client client = mapper.convertValue(clientDTORequest, Client.class);
        Client save = clientRepository.save(client);
        return mapper.convertValue(save, ClientDTORequest.class);
    }

    @Override
    public void deleteClientById(Long id) {
        Client client = getClient(id);
        client.setStatus(CRUDStatus.DELETED);
        client.setUpdatedAt(LocalDateTime.now());
        clientRepository.save(client);
    }

    private Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Клиент с таким ID не найден"));
    }
}
