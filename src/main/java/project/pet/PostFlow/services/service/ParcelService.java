package project.pet.PostFlow.services.service;

import project.pet.PostFlow.model.dto.ParcelDTO;
import project.pet.PostFlow.model.entity.Parcel;

import java.util.List;

public interface ParcelService {
    ParcelDTO getParcelById(Long id);
    List<Parcel> getAllParcels();
    ParcelDTO createParcel(ParcelDTO parcelDTO);
    ParcelDTO updateParcel(Long id, ParcelDTO parcelDTO);
    boolean deleteParcel(Long id);
}
