package project.pet.PostFlow.services.service;

import project.pet.PostFlow.model.dto.ClientDTO;
import project.pet.PostFlow.model.entity.Client;

import java.util.List;

public interface ClientService {
    ClientDTO updateClient(ClientDTO clientDTO);
    ClientDTO createClient(ClientDTO clientDTO);
    List<Client> getAllClients();
    ClientDTO getClientById(Long id);
    Client addClient(Client client);
    Client saveClient(Client client);
    void deleteClientById(Long id);
}
