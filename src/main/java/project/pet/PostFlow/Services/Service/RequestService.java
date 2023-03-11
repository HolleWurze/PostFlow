package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTO;
import project.pet.PostFlow.Model.DTO.RequestDTO;
import project.pet.PostFlow.Model.Entity.Request;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestService {
    RequestDTO createRequest(ClientDTO clientDTO, RequestType requestType, String appointmentTime);
    RequestDTO getRequestById(Long id);
    List<Request> getAllRequests();
    RequestDTO updateRequest(RequestDTO requestDTO);
    boolean deleteRequestById(Long id);
    boolean deleteRequestsByDepartmentId(Long departmentId);
}
