package project.pet.PostFlow.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.CustomException.ResourceNotFoundException;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Services.Service.ClientService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable(value = "id") Long clientId) {
        Client client = clientService.getClientById(clientId);
        return ResponseEntity.ok().body(client);
    }

    @PostMapping
    public Client createClient(@Valid @RequestBody Client client) {
        return clientService.createClient(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable(value = "id") Long clientId, @Valid @RequestBody Client clientDetails) throws ResourceNotFoundException {
        Client client = clientService.getClientById(clientId);
        client.setFirstName(clientDetails.getFirstName());
        client.setLastName(clientDetails.getLastName());
        client.setDepartment(clientDetails.getDepartment());
        client.setUniqueNumber(clientDetails.getUniqueNumber());

        final Client updatedClient = clientService.updateClient(clientId, client);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteClient(@PathVariable(value = "id") Long clientId) throws ResourceNotFoundException {
        clientService.deleteClientById(clientId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
