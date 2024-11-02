package com.example.MediSched.controller;

import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.service.MedicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MedicControllerTest {

    @Mock
    private MedicService medicService;

    @InjectMocks
    private MedicController medicController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateMedicSuccessfully() {
        String crm = "12345";
        MedicDTO medicDTO = new MedicDTO();
        doNothing().when(medicService).updateMedic(crm, medicDTO);

        ResponseEntity<String> response = medicController.updateMedic(crm, medicDTO);

        assertEquals("Medic updated successfully", response.getBody());
        verify(medicService, times(1)).updateMedic(crm, medicDTO);
    }

    @Test
    void getMedicByCrm() {
        String crm = "12345";
        MedicDTO medicDTO = new MedicDTO();
        when(medicService.getMedicByCrm(crm)).thenReturn(medicDTO);

        ResponseEntity<MedicDTO> response = medicController.getMedic(crm);

        assertEquals(medicDTO, response.getBody());
        verify(medicService, times(1)).getMedicByCrm(crm);
    }

    @Test
    void listMedicByCrm() {
        String crm = "12345";
        List<MedicDTO> medics = Collections.emptyList();
        when(medicService.listMedicsByCrm(crm)).thenReturn(medics);

        ResponseEntity<List<MedicDTO>> response = medicController.listMedicByCrm(crm);

        assertEquals(medics, response.getBody());
        verify(medicService, times(1)).listMedicsByCrm(crm);
    }

    @Test
    void listAllMedics() {
        List<MedicDTO> medics = Collections.emptyList();
        when(medicService.getAllMedics()).thenReturn(medics);

        ResponseEntity<List<MedicDTO>> response = medicController.listAllMedics();

        assertEquals(medics, response.getBody());
        verify(medicService, times(1)).getAllMedics();
    }
}