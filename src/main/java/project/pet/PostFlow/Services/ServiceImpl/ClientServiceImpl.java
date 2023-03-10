package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.CustomException.AlreadyExistsException;
import project.pet.PostFlow.CustomException.NotFoundException;
import project.pet.PostFlow.Enum.CRUDStatus;
import project.pet.PostFlow.Model.DTO.ClientDTO;
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
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        return mapper.convertValue(client, ClientDTO.class);
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
    public ClientDTO updateClient(ClientDTO clientDTO) {
        Client existingClient = clientRepository.findById(clientDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Клиент с таким ID не найден " + clientDTO.getId()));
        existingClient.setFirstName(clientDTO.getFirstName());
        existingClient.setLastName(clientDTO.getLastName());
        existingClient.setDepartment(clientDTO.getDepartment());

        return mapper.convertValue(clientRepository.save(existingClient), ClientDTO.class);
    }

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        clientRepository.findById(clientDTO.getId()).ifPresent(
                c -> {
                    throw new AlreadyExistsException("Клиент с таким ID уже существует ", HttpStatus.BAD_REQUEST);
                }
        );

        Client client = mapper.convertValue(clientDTO, Client.class);
        Client save = clientRepository.save(client);
        return mapper.convertValue(save, ClientDTO.class);
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
