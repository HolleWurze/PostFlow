package project.pet.PostFlow.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.CustomException.ResourceNotFoundException;
import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Services.Service.ClientService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    private ModelMapper modelMapper;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTORequest> getClientById(@PathVariable(value = "id") Long clientId) {
        ClientDTORequest clientDTORequest = new ClientDTORequest();
        clientDTORequest.setId(clientId);
        ClientDTORequest client = clientService.getClientById(clientId);
        return ResponseEntity.ok().body(client);
    }

    @PostMapping
    public ResponseEntity<ClientDTORequest> createClient(@RequestBody ClientDTORequest clientDTORequest) {
        return ResponseEntity.ok(clientService.createClient(clientDTORequest));
    }

    @PutMapping
    public ResponseEntity<ClientDTORequest> updateClient(@RequestBody ClientDTORequest clientDTORequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(clientService.updateClient(clientDTORequest));
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteClient(@PathVariable(value = "id") Long clientId) throws ResourceNotFoundException {
        clientService.deleteClientById(clientId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
