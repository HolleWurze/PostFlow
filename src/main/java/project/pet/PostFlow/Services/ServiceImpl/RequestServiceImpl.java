package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.Enum.ClientPriority;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTO;
import project.pet.PostFlow.Model.DTO.RequestDTO;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.ClientRepository;
import project.pet.PostFlow.Model.Repository.RequestRepository;
import project.pet.PostFlow.Services.Service.RequestService;

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
        int estimatedTime = 0;
        switch (requestType) {
            case GET_PARCEL:
                estimatedTime = 10; // minutes
                break;
            case SEND_PARCEL:
                estimatedTime = 20; // minutes
                break;
        }
        return String.valueOf(estimatedTime);
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
    public RequestDTO updateRequest(RequestDTO requestDTO) {     //компилятор требует добавить в реквест @NotNUll, но реквест создается при создании запроса Client?
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
