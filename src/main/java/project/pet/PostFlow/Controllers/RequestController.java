package project.pet.PostFlow.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.CustomException.NotFoundException;
import project.pet.PostFlow.CustomException.ResourceNotFoundException;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Services.Service.ClientService;
import project.pet.PostFlow.Services.Service.RequestService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requests")
public class RequestController {
    private RequestService requestService;
    private ClientService clientService;

    @PostMapping("")
    public ResponseEntity<Request> createRequest(@RequestBody Map<String, String> requestParams) {
        Long clientId = Long.parseLong(requestParams.get("clientId"));
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            throw new NotFoundException("Клиент с id " + clientId + " не найден");
        }
        RequestType requestType = RequestType.valueOf(requestParams.get("requestType"));
        String appointmentTime = requestParams.get("appointmentTime");
        Request request = requestService.createRequest(client, requestType, appointmentTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Request request = requestService.getRequestById(id);
        if (request == null) {
            throw new ResourceNotFoundException("Request not found for this id :: " + id);
        }
        return ResponseEntity.ok(request);
    }

    @GetMapping("")
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getAllRequests();
        if (requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(@PathVariable("id") Long id, @RequestBody Request request) throws ResourceNotFoundException {
        request.setId(id);
        Request updatedRequest = requestService.updateRequest(request);
        if (updatedRequest == null) {
            throw new ResourceNotFoundException("Request not found for this id :: " + id);
        }
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("id") Long id) throws ResourceNotFoundException {
        boolean deleted = requestService.deleteRequestById(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Request not found for this id :: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}
