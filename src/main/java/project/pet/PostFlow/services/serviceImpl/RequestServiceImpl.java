package project.pet.PostFlow.services.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.enums.ClientPriority;
import project.pet.PostFlow.enums.RequestType;
import project.pet.PostFlow.model.dto.ClientDTO;
import project.pet.PostFlow.model.dto.RequestDTO;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.entity.Request;
import project.pet.PostFlow.model.repository.ClientRepository;
import project.pet.PostFlow.model.repository.RequestRepository;
import project.pet.PostFlow.services.service.RequestService;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper mapper;


    @Override
    public RequestDTO createRequest(ClientDTO clientDTO, RequestType requestType, String appointmentTime) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setClientPriority(clientDTO.getClientPriority());
        Client savedClient = clientRepository.save(client);
        Request request = new Request(savedClient, requestType, appointmentTime);
        if (clientDTO.getClientPriority() == ClientPriority.PRIORITY) {
            request.setWaitingTime(String.valueOf(Duration.ZERO));
        } else {
            String estimatedTime = calculateEstimatedTime(request);
            request.setEstimatedTime(estimatedTime);
            request.setWaitingTime(getTotalWaitingTime(request.getClient()));
        }

        return mapper.convertValue(requestRepository.save(request), RequestDTO.class);
    }

    private String getTotalWaitingTime(Client client) {
        Duration totalWaitingTime = Duration.ZERO;
        List<Request> requests = client.getRequests();
        if (requests != null) {
            for (Request request : requests) {
                if (request.getAppointmentTime() == null) {
                    totalWaitingTime = totalWaitingTime.plus(Duration.parse(request.getEstimatedTime()));
                }
            }
        }
        return totalWaitingTime.toString();
    }

    private String calculateEstimatedTime(Request request) {
        RequestType requestType = request.getRequestType();
        Duration estimatedTime = Duration.ZERO;
        switch (requestType) {
            case GET_PARCEL:
                estimatedTime = Duration.ofMinutes(10);
                break;
            case SEND_PARCEL:
                estimatedTime = Duration.ofMinutes(20);
                break;
        }
        return estimatedTime.toString();
    }

    @Override
    public RequestDTO getRequestById(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запрос с таким ID не найден " + id));
        return modelMapper.map(request, RequestDTO.class);
    }

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public RequestDTO updateRequest(RequestDTO requestDTO) {
        Request existingRequest = requestRepository.findById(requestDTO.getId())
                .orElse(null);

        if (existingRequest != null) {
            existingRequest.setDepartment(requestDTO.getDepartment());
            existingRequest.setParcel(requestDTO.getParcel());
            existingRequest.setWaitingTime(requestDTO.getWaitingTime());
            existingRequest.setEstimatedTime(requestDTO.getEstimatedTime());
            existingRequest.setRequestType(requestDTO.getRequestType());

            return mapper.convertValue(requestRepository.save(existingRequest), RequestDTO.class);
        }
        return null;
    }

    @Override
    public boolean deleteRequestById(Long id) {
        Optional<Request> request = requestRepository.findById(id);
        if (request.isPresent()) {
            requestRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRequestsByDepartmentId(Long departmentId) {
        requestRepository.deleteByDepartmentId(departmentId);
        return true;
    }
}
