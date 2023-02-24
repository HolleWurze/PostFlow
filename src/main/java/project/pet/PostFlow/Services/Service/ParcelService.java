package project.pet.PostFlow.Services.Service;

import project.pet.PostFlow.Model.Entity.Parcel;

import java.util.List;

public interface ParcelService {
    Parcel getParcelById(Long id);
    List<Parcel> getAllParcels();
    Parcel createParcel(Parcel parcel);
    Parcel updateParcel(Long id, Parcel parcel);
    boolean deleteParcel(Long id);
}
