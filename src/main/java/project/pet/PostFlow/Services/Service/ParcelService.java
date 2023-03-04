package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.DTO.ParcelDTORequest;
import project.pet.PostFlow.Model.Entity.Parcel;

import java.util.List;

public interface ParcelService {
    Parcel getParcelById(Long id);
    List<Parcel> getAllParcels();
    ParcelDTORequest createParcel(ParcelDTORequest parcelDTORequest);
    ParcelDTORequest updateParcel(Long id, ParcelDTORequest parcelDTORequest);
    boolean deleteParcel(Long id);
}
