package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.Entity.Client;

import java.util.List;

public interface ClientService {

    ClientDTORequest updateClient(ClientDTORequest clientDTORequest);

    ClientDTORequest createClient(ClientDTORequest clientDTORequest);

    List<Client> getAllClients();

    ClientDTORequest getClientById(Long id);

    Client addClient(Client client);

    Client saveClient(Client client);

    void deleteClientById(Long id);
}
