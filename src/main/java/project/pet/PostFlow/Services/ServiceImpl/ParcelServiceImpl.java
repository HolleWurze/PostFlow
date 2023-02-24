package project.pet.PostFlow.Services.ServiceImpl;

import org.springframework.stereotype.Service;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Model.Repository.ParcelRepository;
import project.pet.PostFlow.Services.Service.ParcelService;

import java.util.List;

@Service
public class ParcelServiceImpl implements ParcelService {
    private final ParcelRepository parcelRepository;

    public ParcelServiceImpl(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    @Override
    public Parcel getParcelById(Long id) {
        return parcelRepository.findById(id).orElse(null);
    }

    @Override
    public List<Parcel> getAllParcels() {
        return parcelRepository.findAll();
    }

    @Override
    public Parcel createParcel(Parcel parcel) {
        return parcelRepository.save(parcel);
    }

    @Override
    public Parcel updateParcel(Long id, Parcel parcel) {
        Parcel existingParcel = parcelRepository.findById(id).orElse(null);
        if (existingParcel != null) {
            existingParcel.setClient(parcel.getClient());
            existingParcel.setDepartment(parcel.getDepartment());
            existingParcel.setWeight(parcel.getWeight());
            existingParcel.setStatus(parcel.getStatus());
            return parcelRepository.save(existingParcel);
        } else {
            return null;
        }
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
