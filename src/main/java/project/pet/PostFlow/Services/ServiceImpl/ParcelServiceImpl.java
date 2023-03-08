package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.CustomException.AlreadyExistsException;
import project.pet.PostFlow.Model.DTO.ClientDTORequest;
import project.pet.PostFlow.Model.DTO.ParcelDTORequest;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Model.Repository.ParcelRepository;
import project.pet.PostFlow.Services.Service.ParcelService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {
    private final ParcelRepository parcelRepository;
    private final ObjectMapper mapper;

    @Override
    public ParcelDTORequest getParcelById(Long id) {
        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Посылка не найдена с этим ID: " + id));
        return mapper.convertValue(parcel, ParcelDTORequest.class);
    }

    @Override
    public List<Parcel> getAllParcels() {
        return parcelRepository.findAll();
    }

    @Override
    public ParcelDTORequest createParcel(ParcelDTORequest parcelDTORequest) {
        parcelRepository.findById(parcelDTORequest.getId()).ifPresent(
                c -> {
                    throw new AlreadyExistsException("Посылка с таким ID уже существует ", HttpStatus.BAD_REQUEST);
                }
        );
        Parcel parcel = mapper.convertValue(parcelDTORequest, Parcel.class);
        Parcel save = parcelRepository.save(parcel);
        return mapper.convertValue(save, ParcelDTORequest.class);
    }

    @Override
    public ParcelDTORequest updateParcel(Long id, ParcelDTORequest parcelDTORequest) {
        Parcel existingParcel = parcelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент с таким ID не найден " + parcelDTORequest.getId()));
//        if (existingParcel != null) {
            existingParcel.setClient(parcelDTORequest.getClient());
            existingParcel.setDepartment(parcelDTORequest.getDepartment());
            existingParcel.setWeight(parcelDTORequest.getWeight());
            existingParcel.setStatus(parcelDTORequest.getStatus());
            return mapper.convertValue(parcelRepository.save(existingParcel), ParcelDTORequest.class);
//        } else {
//            return null;
//        }
    }

    @Override
    public boolean deleteParcel(Long id) {
        Parcel parcel = parcelRepository.findById(id).orElse(null);
        if (parcel != null) {
            parcelRepository.delete(parcel);
            return true;
        } else {
            return false;
        }
    }
}
