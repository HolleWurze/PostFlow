package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.Entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();

    Client getClientById(Long id);

    Client addClient(Client client);

    Client saveClient(Client client);

    Client updateClient(Long id, Client client);

    Client createClient(Client client);

    void deleteClientById(Long id);
}
