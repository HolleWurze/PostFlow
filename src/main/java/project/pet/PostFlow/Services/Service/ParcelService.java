package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.DTO.ParcelDTO;
import project.pet.PostFlow.Model.Entity.Parcel;

import java.util.List;

public interface ParcelService {
    ParcelDTO getParcelById(Long id);
    List<Parcel> getAllParcels();
    ParcelDTO createParcel(ParcelDTO parcelDTO);
    ParcelDTO updateParcel(Long id, ParcelDTO parcelDTO);
    boolean deleteParcel(Long id);
}
