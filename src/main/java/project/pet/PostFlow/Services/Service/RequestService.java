package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.Entity.Request;

import java.util.List;

public interface RequestService {
    Request createRequest(Request request);
    Request getRequestById(Long id);
    List<Request> getAllRequests();
    Request updateRequest(Request request);
    boolean deleteRequestById(Long id);
    boolean deleteRequestsByDepartmentId(Long departmentId);
}
