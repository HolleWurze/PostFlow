package project.pet.PostFlow.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.CustomException.ResourceNotFoundException;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Services.Service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {
    private RequestService requestService;

    @PostMapping("")
    public ResponseEntity<Request> createRequest(@RequestBody Request request) {
        Request createdRequest = requestService.createRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
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
