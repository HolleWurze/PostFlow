package project.pet.PostFlow.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pet.PostFlow.model.dto.ParcelDTO;
import project.pet.PostFlow.model.entity.Parcel;
import project.pet.PostFlow.services.service.ParcelService;

import java.util.List;

@RestController
@RequestMapping("/parcels")
@RequiredArgsConstructor
public class ParcelController {
    private final ParcelService parcelService;
    private final ObjectMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<ParcelDTO> getParcelById(@PathVariable Long id) {
        ParcelDTO parcelDTORequest = new ParcelDTO();
        parcelDTORequest.setId(id);
        ParcelDTO parcel = parcelService.getParcelById(id);
        return ResponseEntity.ok().body(parcel);
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
    public ResponseEntity<ParcelDTO> createParcel(@RequestBody ParcelDTO parcelDTO) {
        ParcelDTO createdParcel = parcelService.createParcel(parcelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParcel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParcelDTO> updateParcel(@PathVariable Long id, @RequestBody ParcelDTO parcelDTO) {
        ParcelDTO updatedParcel = parcelService.updateParcel(id, parcelDTO);
        if (updatedParcel != null) {
            return ResponseEntity.ok(parcelService.updateParcel(id, updatedParcel));
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
