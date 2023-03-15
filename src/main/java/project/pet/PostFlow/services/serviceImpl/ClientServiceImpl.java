package project.pet.PostFlow.services.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.customException.AlreadyExistsException;
import project.pet.PostFlow.customException.NonValidException;
import project.pet.PostFlow.customException.NotFoundException;
import project.pet.PostFlow.enums.CRUDStatus;
import project.pet.PostFlow.model.dto.ClientDTO;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.repository.ClientRepository;
import project.pet.PostFlow.services.service.ClientService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.internet.InternetAddress;

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
    public ClientDTO updateClient(ClientDTO clientDTORequest) {
        Client existingClient = clientRepository.findById(clientDTORequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Клиент с таким ID не найден " + clientDTORequest.getId()));
        existingClient.setFirstName(clientDTORequest.getFirstName());
        existingClient.setLastName(clientDTORequest.getLastName());
        existingClient.setDepartment(clientDTORequest.getDepartment());

        return mapper.convertValue(clientRepository.save(existingClient), ClientDTO.class);
    }

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        clientRepository.findByUserName(clientDTO.getUserName()).ifPresent(
                c -> {
                    throw new AlreadyExistsException("Клиент с таким именем пользователя уже существует ", HttpStatus.BAD_REQUEST);
                }
        );

        if (clientDTO.getPassword().length() < 8) {
            log.error("[Create Client] Password is not valid" + clientDTO);
            throw new NonValidException("Пароль должен быть больше 7 символов", HttpStatus.BAD_REQUEST);
        }
        try {
            InternetAddress emailAddr = new InternetAddress(clientDTO.getEmail());
            emailAddr.validate();
        } catch (Exception ex) {
            log.error("[Create User] email is not valid" + clientDTO);
            throw new NonValidException("Невалидный email", HttpStatus.BAD_REQUEST);
        }
        clientRepository.findByEmail(clientDTO.getEmail()).ifPresent(
                driver -> {
                    log.error("[Create User] User already existed" + clientDTO);
                    throw new NonValidException("Пользователь с таким email уже существует", HttpStatus.BAD_REQUEST);
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
        clientRepository.delete(client);
    }

    private Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Клиент с таким ID не найден"));
    }
}
