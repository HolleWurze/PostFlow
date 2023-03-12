package project.pet.PostFlow.Controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.CustomException.ResourceNotFoundException;
import project.pet.PostFlow.Model.DTO.ClientDTO;;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Repository.ClientRepository;
import project.pet.PostFlow.Services.Service.ClientService;

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
    public ResponseEntity<ClientDTO> getClientById(@PathVariable(value = "id") Long clientId) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(clientId);
        ClientDTO client = clientService.getClientById(clientId);
        return ResponseEntity.ok().body(client);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.createClient(clientDTO));
    }

    @PutMapping
    public ResponseEntity<ClientDTO> updateClient(@RequestBody ClientDTO clientDTO) throws ResourceNotFoundException {
        return ResponseEntity.ok(clientService.updateClient(clientDTO));
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteClient(@PathVariable(value = "id") Long clientId) throws ResourceNotFoundException {
        clientService.deleteClientById(clientId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
