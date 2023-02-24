package project.pet.PostFlow.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Services.Service.ParcelService;

import java.util.List;

@RestController
@RequestMapping("/parcels")
public class ParcelController {
    private ParcelService parcelService;

    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parcel> getParcelById(@PathVariable Long id) {
        Parcel parcel = parcelService.getParcelById(id);
        if (parcel != null) {
            return ResponseEntity.ok(parcel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Parcel>> getAllParcels() {
        List<Parcel> parcels = parcelService.getAllParcels();
        if (!parcels.isEmpty()) {
            return ResponseEntity.ok(parcels);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Parcel> createParcel(@RequestBody Parcel parcel) {
        Parcel createdParcel = parcelService.createParcel(parcel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParcel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parcel> updateParcel(@PathVariable Long id, @RequestBody Parcel parcel) {
        Parcel updatedParcel = parcelService.updateParcel(id, parcel);
        if (updatedParcel != null) {
            return ResponseEntity.ok(updatedParcel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParcel(@PathVariable Long id) {
        if (parcelService.deleteParcel(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
