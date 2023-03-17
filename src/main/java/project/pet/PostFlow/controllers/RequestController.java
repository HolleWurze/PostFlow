package project.pet.PostFlow.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.customException.NotFoundException;
import project.pet.PostFlow.customException.ResourceNotFoundException;
import project.pet.PostFlow.enums.RequestType;
import project.pet.PostFlow.model.dto.ClientDTO;
import project.pet.PostFlow.model.dto.RequestDTO;
import project.pet.PostFlow.model.entity.Request;
import project.pet.PostFlow.services.service.ClientService;
import project.pet.PostFlow.services.service.RequestService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final ClientService clientService;

    @PostMapping("/create_request")
    public ResponseEntity<RequestDTO> createRequest(@RequestBody Map<String, String> requestParams) {
        Long clientId = Long.parseLong(requestParams.get("clientId"));
        ClientDTO client = clientService.getClientById(clientId);
        if (client == null) {
            throw new NotFoundException("Клиент с id " + clientId + " не найден");
        }
        RequestType requestType = RequestType.valueOf(requestParams.get("requestType"));
        String appointmentTime = requestParams.get("appointmentTime");
        RequestDTO request = requestService.createRequest(client, requestType, appointmentTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getRequestById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        RequestDTO request = requestService.getRequestById(id);
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
    public ResponseEntity<RequestDTO> updateRequest(@PathVariable("id") Long id, @RequestBody RequestDTO requestDTO) throws ResourceNotFoundException {
        requestDTO.setId(id);
        RequestDTO updatedRequest = requestService.updateRequest(requestDTO);
        if (updatedRequest == null) {
            throw new ResourceNotFoundException("Запрос не найден с этим ID :: " + id);
        }
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/delete_from_queue")
    public ResponseEntity<Void> deleteRequest(@PathVariable("id") Long id) throws ResourceNotFoundException {
        boolean deleted = requestService.deleteRequestById(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Запрос не найден с этим ID :: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}
