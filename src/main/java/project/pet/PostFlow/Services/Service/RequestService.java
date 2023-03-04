package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Enum.RequestType;
import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.DTO.RequestDTORequest;
import project.pet.PostFlow.Model.Entity.Request;

import java.util.List;

public interface RequestService {
    Request createRequest(ClientDTORequest clientDTORequest, RequestType requestType, String appointmentTime);
    Request getRequestById(Long id);
    List<Request> getAllRequests();
    RequestDTORequest updateRequest(RequestDTORequest requestDTORequest);
    boolean deleteRequestById(Long id);
    boolean deleteRequestsByDepartmentId(Long departmentId);
}
