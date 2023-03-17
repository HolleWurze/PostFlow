package project.pet.PostFlow.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.enums.RequestType;
import project.pet.PostFlow.model.dto.ClientDTO;
import project.pet.PostFlow.model.dto.RequestDTO;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.entity.Request;
import project.pet.PostFlow.model.repository.ClientRepository;
import project.pet.PostFlow.model.repository.RequestRepository;
import project.pet.PostFlow.services.service.ClientService;
import project.pet.PostFlow.services.service.QueueService;

import java.util.List;

@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class QueueController {
    private final QueueService queueService;
    private final ClientService clientService;
    private final RequestRepository requestRepository;
    private final ClientRepository clientRepository;

    @GetMapping("/position")
    public String getQueueInfo(Model model, @RequestParam Long clientId) {
        RequestDTO currentRequest = queueService.getCurrentRequest();
        if (currentRequest == null || currentRequest.getClient().getId() != clientId) {
            model.addAttribute("queueSize", 0);
        } else {
            List<Client> clientQueue = queueService.getClientQueue();
            int queuePosition = clientQueue.indexOf(currentRequest.getClient()) + 1;
            model.addAttribute("queueSize", clientQueue.size());
            model.addAttribute("queuePosition", queuePosition);
            model.addAttribute("estimatedWaitingTime", currentRequest.getEstimatedTime());
        }
        return "queue-info";
    }

    @PostMapping("/add")
    public ResponseEntity<RequestDTO> addRequest(@RequestBody Request request) {
        ClientDTO client = clientService.getClientById(request.getClient().getId());
        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        RequestType requestType = request.getRequestType();
        String appointmentTime = request.getAppointmentTime();

        RequestDTO newRequest = queueService.addRequest(client, requestType, appointmentTime);

        if (newRequest == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(newRequest);
    }

    @GetMapping("/current")
    public ResponseEntity<RequestDTO> getCurrentRequest() {
        RequestDTO currentRequest = queueService.getCurrentRequest();
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
    public void recalculateEstimatedTime(@RequestParam Long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client != null) {
            queueService.recalculateEstimatedTime(client);
        }
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
