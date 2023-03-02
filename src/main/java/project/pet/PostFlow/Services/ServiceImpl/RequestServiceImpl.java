package project.pet.PostFlow.Services.ServiceImpl;

import org.springframework.stereotype.Service;
import project.pet.PostFlow.Model.Entity.Request;
import project.pet.PostFlow.Model.Repository.RequestRepository;
import project.pet.PostFlow.Services.Service.RequestService;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Request createRequest(Request request) {
        return requestRepository.save(request);
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
    public Request updateRequest(Request request) {
        Request existingRequest = requestRepository.findById(request.getId())
                .orElse(null);

        if (existingRequest != null) {
            existingRequest.setSender(request.getSender());
            existingRequest.setRecipient(request.getRecipient());
            existingRequest.setDepartment(request.getDepartment());

            return requestRepository.save(existingRequest);
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
