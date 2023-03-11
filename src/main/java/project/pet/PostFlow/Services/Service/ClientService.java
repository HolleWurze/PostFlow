package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.DTO.ClientDTO;
import project.pet.PostFlow.Model.Entity.Client;

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
