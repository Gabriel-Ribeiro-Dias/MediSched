package com.example.MediSched.service;

import com.example.MediSched.exceptions.MedicAlreadyExistsException;
import com.example.MediSched.exceptions.MedicNotFoundException;
import com.example.MediSched.model.Medic;
import com.example.MediSched.model.User;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.repository.MedicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MedicServiceTest {

    @Mock
    private MedicRepository medicRepository;

    @InjectMocks
    private MedicService medicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMedicSuccessfully() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("John Doe");
        medicDTO.setCrm("12345");
        medicDTO.setExpertise("Cardiology");
        User user = new User();
        when(medicRepository.findByCrm(medicDTO.getCrm())).thenReturn(Optional.empty());
        when(medicRepository.save(any(Medic.class))).thenReturn(new Medic());

        Medic result = medicService.create(medicDTO, user);

        assertNotNull(result);
        verify(medicRepository, times(1)).save(any(Medic.class));
    }

    @Test
    void createMedicThrowsExceptionWhenCrmExists() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("John Doe");
        medicDTO.setCrm("12345");
        medicDTO.setExpertise("Cardiology");
        User user = new User();
        when(medicRepository.findByCrm(medicDTO.getCrm())).thenReturn(Optional.of(new Medic()));

        assertThrows(MedicAlreadyExistsException.class, () -> medicService.create(medicDTO, user));
    }

    @Test
    void getAllMedicsReturnsList() {
        when(medicRepository.findAll()).thenReturn(List.of(new Medic(), new Medic()));

        List<MedicDTO> result = medicService.getAllMedics();

        assertEquals(2, result.size());
    }

    @Test
    void getMedicByCrmSuccessfully() {
        Medic medic = new Medic();
        medic.setCrm("12345");
        when(medicRepository.findByCrm("12345")).thenReturn(Optional.of(medic));

        MedicDTO result = medicService.getMedicByCrm("12345");

        assertNotNull(result);
        assertEquals("12345", result.getCrm());
    }

    @Test
    void getMedicByCrmThrowsExceptionWhenNotFound() {
        when(medicRepository.findByCrm("12345")).thenReturn(Optional.empty());

        assertThrows(MedicNotFoundException.class, () -> medicService.getMedicByCrm("12345"));
    }

    @Test
    void updateMedicSuccessfully() {
        Medic medic = new Medic();
        medic.setCrm("12345");
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("John Doe");
        medicDTO.setCrm("12345");
        medicDTO.setExpertise("Cardiology");
        when(medicRepository.findByCrm("12345")).thenReturn(Optional.of(medic));

        medicService.updateMedic("12345", medicDTO);

        verify(medicRepository, times(1)).save(medic);
    }

    @Test
    void updateMedicThrowsExceptionWhenNotFound() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("John Doe");
        medicDTO.setCrm("12345");
        medicDTO.setExpertise("Cardiology");
        when(medicRepository.findByCrm("12345")).thenReturn(Optional.empty());

        assertThrows(MedicNotFoundException.class, () -> medicService.updateMedic("12345", medicDTO));
    }

    @Test
    void deleteMedicSuccessfully() {
        Medic medic = new Medic();
        medic.setCrm("12345");
        when(medicRepository.findByCrm("12345")).thenReturn(Optional.of(medic));

        medicService.deleteMedic("12345");

        verify(medicRepository, times(1)).deleteById(medic.getId());
    }

    @Test
    void deleteMedicThrowsExceptionWhenNotFound() {
        when(medicRepository.findByCrm("12345")).thenReturn(Optional.empty());

        assertThrows(MedicNotFoundException.class, () -> medicService.deleteMedic("12345"));
    }

    @Test
    void listMedicsByCrmSuccessfully() {
        Medic medic = new Medic();
        medic.setCrm("12345");
        when(medicRepository.findAllByCrm("12345")).thenReturn(List.of(medic));

        List<MedicDTO> result = medicService.listMedicsByCrm("12345");

        assertEquals(1, result.size());
    }

    @Test
    void listMedicsByCrmThrowsExceptionWhenCrmIsNull() {
        assertThrows(IllegalArgumentException.class, () -> medicService.listMedicsByCrm(null));
    }
}