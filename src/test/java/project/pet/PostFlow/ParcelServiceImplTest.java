package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Copy;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import project.pet.PostFlow.Enum.Status;
import project.pet.PostFlow.Model.DTO.ParcelDTORequest;
import project.pet.PostFlow.Model.Entity.Client;
import project.pet.PostFlow.Model.Entity.Department;
import project.pet.PostFlow.Model.Entity.Parcel;
import project.pet.PostFlow.Model.Repository.ParcelRepository;
import project.pet.PostFlow.Services.ServiceImpl.ParcelServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParcelServiceImplTest {

    @Spy
    ObjectMapper mapper;

    @Mock
    ParcelRepository parcelRepository;

    @InjectMocks
    ParcelServiceImpl parcelService;

    private ParcelDTORequest createTestParcelDTORequest() {
        ParcelDTORequest parcelDTORequest = new ParcelDTORequest();
        Client client = new Client();
        Department department = new Department();
        parcelDTORequest.setId(1L);
        parcelDTORequest.setClient(client);
        parcelDTORequest.setDepartment(department);
        parcelDTORequest.setWeight(2.5);
        parcelDTORequest.setStatus(Status.DELIVERED);
        return parcelDTORequest;
    }

    private Parcel createTestParcel() {
        Client client = new Client();
        Department department = new Department();
        Parcel parcel = new Parcel();
        parcel.setId(1L);
        parcel.setClient(client);
        parcel.setDepartment(department);
        parcel.setWeight(2.5);
        parcel.setStatus(Status.DELIVERED);
        return parcel;
    }

    @Test
    public void testGetParcelById() {
        Parcel expectedParcel = createTestParcel();
        when(parcelRepository.findById(expectedParcel.getId())).thenReturn(Optional.of(expectedParcel));

        ParcelDTORequest actualParcel = parcelService.getParcelById(expectedParcel.getId());

        assertEquals(expectedParcel, actualParcel);
        verify(parcelRepository, times(1)).findById(expectedParcel.getId());
    }

    @Test
    public void testGetAllParcels() {
        List<Parcel> expectedParcels = Arrays.asList(createTestParcel());
        when(parcelRepository.findAll()).thenReturn(expectedParcels);

        List<Parcel> actualParcels = parcelService.getAllParcels();

        assertEquals(expectedParcels, actualParcels);
        verify(parcelRepository, times(1)).findAll();
    }

    @Test
    public void testCreateParcel() {
        ParcelDTORequest parcelDTORequest = new ParcelDTORequest();
        parcelDTORequest.setId(1L);
        parcelDTORequest.setDescription("Тестовая посылка");
        parcelDTORequest.setWeight(1.0);

        when(parcelRepository.findById(1L)).thenReturn(Optional.empty());
        when(parcelRepository.save(any(Parcel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParcelDTORequest createdParcel = parcelService.createParcel(parcelDTORequest);

        assertNotNull(createdParcel);
        assertEquals(parcelDTORequest.getDescription(), createdParcel.getDescription());
        assertEquals(parcelDTORequest.getWeight(), createdParcel.getWeight());

        verify(parcelRepository, times(1)).findById(1L);
        verify(parcelRepository, times(1)).save(any(Parcel.class));

    }

    @Test
    public void testUpdateParcel() {
        Parcel parcelToUpdate = new Parcel();
        parcelToUpdate.setId(1L);
        parcelToUpdate.setClient(new Client());
        parcelToUpdate.setDepartment(new Department());
        parcelToUpdate.setWeight(1.0);
        parcelToUpdate.setStatus(Status.DELIVERED);

        ParcelDTORequest updatedParcelDTO = new ParcelDTORequest();
        updatedParcelDTO.setId(1L);
        updatedParcelDTO.setClient(new Client());
        updatedParcelDTO.setDepartment(new Department());
        updatedParcelDTO.setWeight(2.0);
        updatedParcelDTO.setStatus(Status.RETURNED);

        when(parcelRepository.findById(1L)).thenReturn(Optional.of(parcelToUpdate));
        when(parcelRepository.save(any(Parcel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParcelDTORequest updatedParcelDTORequest = parcelService.updateParcel(1L, updatedParcelDTO);

        verify(parcelRepository, times(1)).findById(1L);

        verify(parcelRepository, times(1)).save(eq(parcelToUpdate));

        assertEquals(updatedParcelDTO.getClient(), updatedParcelDTORequest.getClient());
        assertEquals(updatedParcelDTO.getDepartment(), updatedParcelDTORequest.getDepartment());
        assertEquals(updatedParcelDTO.getWeight(), updatedParcelDTORequest.getWeight());
        assertEquals(updatedParcelDTO.getStatus(), updatedParcelDTORequest.getStatus());
    }

    @Test
    public void testDeleteParcel() {
        Long id = 1L;
        Parcel parcel = new Parcel();
        parcel.setId(id);
        when(parcelRepository.findById(id)).thenReturn(Optional.of(parcel));

        boolean isDeleted = parcelService.deleteParcel(id);

        verify(parcelRepository, times(1)).findById(id);
        verify(parcelRepository, times(1)).delete(parcel);
        assertTrue(isDeleted);
    }

    @Test
    public void testDeleteParcelWhenParcelNotFound() {
        Long id = 1L;
        when(parcelRepository.findById(id)).thenReturn(Optional.empty());

        boolean isDeleted = parcelService.deleteParcel(id);

        verify(parcelRepository, times(1)).findById(id);
        verify(parcelRepository, never()).delete(any(Parcel.class));
        assertFalse(isDeleted);
    }

}
