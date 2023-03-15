package project.pet.PostFlow.services.service;

import project.pet.PostFlow.enums.RequestType;
import project.pet.PostFlow.model.dto.ClientDTO;
import project.pet.PostFlow.model.dto.RequestDTO;
import project.pet.PostFlow.model.entity.Request;

import java.util.List;

public interface RequestService {
    RequestDTO createRequest(ClientDTO clientDTO, RequestType requestType, String appointmentTime);
    RequestDTO getRequestById(Long id);
    List<Request> getAllRequests();
    RequestDTO updateRequest(RequestDTO requestDTO);
    boolean deleteRequestById(Long id);
    boolean deleteRequestsByDepartmentId(Long departmentId);
}
