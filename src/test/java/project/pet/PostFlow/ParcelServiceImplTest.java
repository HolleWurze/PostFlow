package project.pet.PostFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import project.pet.PostFlow.customException.AlreadyExistsException;
import project.pet.PostFlow.enums.Status;
import project.pet.PostFlow.model.dto.ParcelDTO;
import project.pet.PostFlow.model.entity.Client;
import project.pet.PostFlow.model.entity.Department;
import project.pet.PostFlow.model.entity.Parcel;
import project.pet.PostFlow.model.repository.ParcelRepository;
import project.pet.PostFlow.services.serviceImpl.ParcelServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParcelServiceImplTest {
    @Mock
    ObjectMapper mapper;
    @Mock
    ParcelRepository parcelRepository;
    @InjectMocks
    ParcelServiceImpl parcelService;

    private ParcelDTO parcelDTO;
    private Parcel parcel;

    @Before
    public void setUp() {
        parcelDTO = createTestParcelDTO();
        parcel = createTestParcel();
    }

    private ParcelDTO createTestParcelDTO() {
        ParcelDTO parcelDTO = new ParcelDTO();
        Client client = new Client();
        Department department = new Department();
        parcelDTO.setId(1L);
        parcelDTO.setClient(client);
        parcelDTO.setDepartment(department);
        parcelDTO.setWeight(2.5);
        parcelDTO.setStatus(Status.DELIVERED);
        return parcelDTO;
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
    public void testGetAllParcels() {
        List<Parcel> expectedParcels = Arrays.asList(createTestParcel());
        when(parcelRepository.findAll()).thenReturn(expectedParcels);

        List<Parcel> actualParcels = parcelService.getAllParcels();

        assertEquals(expectedParcels, actualParcels);
        verify(parcelRepository, times(1)).findAll();
    }

    @Test
    public void testCreateParcel_success() {
        when(parcelRepository.findById(1L)).thenReturn(Optional.empty());
        when(mapper.convertValue(parcelDTO, Parcel.class)).thenReturn(parcel);
        when(parcelRepository.save(parcel)).thenReturn(parcel);
        when(mapper.convertValue(parcel, ParcelDTO.class)).thenReturn(parcelDTO);

        ParcelDTO createdParcelDTO = parcelService.createParcel(parcelDTO);

        assertEquals(parcelDTO, createdParcelDTO);
        verify(parcelRepository).findById(1L);
        verify(mapper).convertValue(parcelDTO, Parcel.class);
        verify(parcelRepository).save(parcel);
        verify(mapper).convertValue(parcel, ParcelDTO.class);
    }

    @Test
    public void testCreateParcel_existingId() {
        when(parcelRepository.findById(1L)).thenReturn(Optional.of(parcel));

        AlreadyExistsException exception = assertThrows(
                AlreadyExistsException.class,
                () -> parcelService.createParcel(parcelDTO)
        );

        assertEquals("Клиент с таким ID уже существует ", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(parcelRepository).findById(1L);
    }

    @Test
    public void testGetParcelById() {
        Long id = 1L;
        Parcel parcel = new Parcel();
        parcel.setId(id);
        parcel.setClient(new Client());
        parcel.setDepartment(new Department());
        parcel.setWeight(1.0);
        parcel.setStatus(Status.NEW);
        ParcelDTO expectedParcelDTO = new ParcelDTO();
        expectedParcelDTO.setId(id);
        expectedParcelDTO.setClient(new Client());
        expectedParcelDTO.setDepartment(new Department());
        expectedParcelDTO.setWeight(1.0);
        expectedParcelDTO.setStatus(Status.NEW);
        when(parcelRepository.findById(id)).thenReturn(Optional.of(parcel));
        when(mapper.convertValue(parcel, ParcelDTO.class)).thenReturn(expectedParcelDTO);

        ParcelDTO actualParcelDTO = parcelService.getParcelById(id);

        assertEquals(expectedParcelDTO, actualParcelDTO);
        verify(parcelRepository, times(1)).findById(id);
        verify(mapper, times(1)).convertValue(parcel, ParcelDTO.class);
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

    @Test
    public void testUpdateParcel() {
        ParcelDTO parcelDTO = new ParcelDTO();
        parcelDTO.setId(1L);
        parcelDTO.setClient(new Client());
        parcelDTO.setDepartment(new Department());
        parcelDTO.setWeight(2.5);
        parcelDTO.setStatus(Status.DELIVERED);

        Parcel existingParcel = new Parcel();
        existingParcel.setId(1L);
        existingParcel.setClient(new Client());
        existingParcel.setDepartment(new Department());
        existingParcel.setWeight(1.5);
        existingParcel.setStatus(Status.RETURNED);

        Parcel updatedParcel = new Parcel();
        updatedParcel.setId(1L);
        updatedParcel.setClient(new Client());
        updatedParcel.setDepartment(new Department());
        updatedParcel.setWeight(2.5);
        updatedParcel.setStatus(Status.DELIVERED);

        when(parcelRepository.findById(1L)).thenReturn(Optional.of(existingParcel));
        when(parcelRepository.save(existingParcel)).thenReturn(updatedParcel);
        when(mapper.convertValue(updatedParcel, ParcelDTO.class)).thenReturn(parcelDTO);

        ParcelDTO result = parcelService.updateParcel(1L, parcelDTO);
        assertNotNull(result);
        assertEquals(parcelDTO, result);

        verify(parcelRepository, times(1)).findById(1L);
        verify(parcelRepository, times(1)).save(existingParcel);
        verify(mapper, times(1)).convertValue(updatedParcel, ParcelDTO.class);
    }
}
