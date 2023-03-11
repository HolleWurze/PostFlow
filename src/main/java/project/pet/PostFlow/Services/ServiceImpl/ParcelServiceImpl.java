package project.pet.PostFlow.Services.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.pet.PostFlow.CustomException.AlreadyExistsException;
import project.pet.PostFlow.Model.DTO.ParcelDTO;
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
    public ParcelDTO getParcelById(Long id) {
        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Посылка не найдена с этим ID: " + id));
        return mapper.convertValue(parcel, ParcelDTO.class);
    }

    @Override
    public List<Parcel> getAllParcels() {
        return parcelRepository.findAll();
    }

    @Override
    public ParcelDTO createParcel(ParcelDTO parcelDTO) {
        log.info("ParcelDTO received: {}", parcelDTO);
        if (parcelRepository.existsById(parcelDTO.getId())) {
            throw new AlreadyExistsException("Посылка с таким ID уже существует ", HttpStatus.BAD_REQUEST);
        }
        log.debug("ParcelDTO fields: id={}, client={}, department={}, weight={}, status={}, trackingNumber={}, description={}",
                parcelDTO.getId(),
                parcelDTO.getClient(),
                parcelDTO.getDepartment(),
                parcelDTO.getWeight(),
                parcelDTO.getStatus(),
                parcelDTO.getTrackingNumber(),
                parcelDTO.getDescription());
        Parcel parcel = mapper.convertValue(parcelDTO, Parcel.class);

//        if (parcel == null) {
//            throw new IllegalArgumentException("Non converted to DTO");
//        }

        Parcel savedParcel = parcelRepository.save(parcel);
        return mapper.convertValue(savedParcel, ParcelDTO.class);
    }

    @Override
    public ParcelDTO updateParcel(Long id, ParcelDTO parcelDTO) {
        Parcel existingParcel = parcelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент с таким ID не найден " + parcelDTO.getId()));
        existingParcel.setClient(parcelDTO.getClient());
        existingParcel.setDepartment(parcelDTO.getDepartment());
        existingParcel.setWeight(parcelDTO.getWeight());
        existingParcel.setStatus(parcelDTO.getStatus());

        return mapper.convertValue(parcelRepository.save(existingParcel), ParcelDTO.class);

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
