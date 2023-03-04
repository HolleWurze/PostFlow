package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.Enum.ClientPriority;
import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.DTO.RequestDTORequest;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.RequestRepository;
import project.pet.PostFlow.Services.Service.ClientService;
import project.pet.PostFlow.Services.Service.RequestService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ClientService clientService;
    private ModelMapper modelMapper;

    private final ObjectMapper mapper;


    @Override
    public Request createRequest(ClientDTORequest clientDTORequest, RequestType requestType, String appointmentTime) {
        Client client = modelMapper.map(clientDTORequest, Client.class);
        String appointmentDateTime = String.valueOf(LocalDateTime.parse(appointmentTime, DateTimeFormatter.ISO_DATE_TIME));

        Request request = new Request(client, requestType, String.valueOf(appointmentDateTime));
        if (clientDTORequest.getClientPriority() == ClientPriority.PRIORITY) {
            request.setWaitingTime(String.valueOf(Duration.ZERO));
        } else {
            String estimatedTime = calculateEstimatedTime(request);
            request.setEstimatedTime(estimatedTime);
            request.setWaitingTime(getTotalWaitingTime(request.getClient()));
        }
        return requestRepository.save(request);
    }

    private String getTotalWaitingTime(Client client) {
        Duration totalWaitingTime = Duration.ZERO;
        for (Request request : client.getRequests()) {
            if (request.getAppointmentDateTime() == null) {
                totalWaitingTime = totalWaitingTime.plus(Duration.parse(request.getEstimatedTime()));
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
    public Request getRequestById(Long id) {
        return requestRepository.findById(id).orElse(null);
    }

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public RequestDTORequest updateRequest(RequestDTORequest requestDTORequest) {     //компилятор требует добавить в реквест @NotNUll, но реквест создается при создании запроса Client?
        Request existingRequest = requestRepository.findById(requestDTORequest.getId())
                .orElse(null);

        if (existingRequest != null) {
            existingRequest.setDepartment(requestDTORequest.getDepartment());
            existingRequest.setParcel(requestDTORequest.getParcel());
            existingRequest.setAppointmentDateTime(requestDTORequest.getAppointmentDateTime());
            existingRequest.setWaitingTime(requestDTORequest.getWaitingTime());
            existingRequest.setEstimatedTime(requestDTORequest.getEstimatedTime());
            existingRequest.setRequestType(requestDTORequest.getRequestType());

            return mapper.convertValue(requestRepository.save(existingRequest), RequestDTORequest.class);
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
