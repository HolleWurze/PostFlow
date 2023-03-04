package project.pet.PostFlow.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.RequestRepository;
import project.pet.PostFlow.Services.Service.ClientService;
import project.pet.PostFlow.Services.Service.QueueService;

import java.util.List;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private QueueService queueService;
    private ClientService clientService;
    private RequestRepository requestRepository;

    @PostMapping("/add")
    public ResponseEntity<Request> addRequest(@RequestBody Request request) {
        ClientDTORequest client = clientService.getClientById(request.getClient().getId());
        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        RequestType requestType = request.getRequestType();
        String appointmentTime = request.getAppointmentTime();

        Request newRequest = queueService.addRequest(client, requestType, appointmentTime);

        if (newRequest == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(newRequest);
    }

    @GetMapping("/current")
    public ResponseEntity<Request> getCurrentRequest() {
        Request currentRequest = queueService.getCurrentRequest();
        if (currentRequest == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(currentRequest);
    }

    @PostMapping("/done")
    public ResponseEntity<Void> markCurrentRequestDone() {
        queueService.markCurrentRequestDone();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/requests")
    public ResponseEntity<List<Request>> getRequests() {
        List<Request> requests = queueService.getRequests();
        if (requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(requests);
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> removeRequest(@RequestParam Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }

        queueService.removeRequest(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/recalculateEstimatedTime")
    public void recalculateEstimatedTime() {
        queueService.recalculateEstimatedTime();
    }

    @PostMapping("/removeFromQueue")
    public ResponseEntity<String> removeFromQueue(@RequestBody Client client) {
        try {
            queueService.removeFromQueue(client);
            return ResponseEntity.ok("Клиент удален из очереди");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/client-queue")
    public ResponseEntity<List<Client>> getClientQueue() {
        List<Client> clientQueue = queueService.getClientQueue();
        return new ResponseEntity<>(clientQueue, HttpStatus.OK);
    }
}
