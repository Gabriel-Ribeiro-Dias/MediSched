package com.example.MediSched.controller;

import com.example.MediSched.model.dto.PatientDTO;
import com.example.MediSched.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPatientsSuccessfully() {
        List<PatientDTO> patients = Collections.emptyList();
        when(patientService.getAllPatients()).thenReturn(patients);

        ResponseEntity<List<PatientDTO>> response = patientController.getAllPatients();

        assertEquals(patients, response.getBody());
        verify(patientService, times(1)).getAllPatients();
    }

    @Test
    void updatePatientSuccessfully() {
        String cpf = "12345678900";
        PatientDTO patientDTO = new PatientDTO();
        doNothing().when(patientService).updatePatient(cpf, patientDTO);

        ResponseEntity<String> response = patientController.updatePatient(cpf, patientDTO);

        assertEquals("Patient updated successfully", response.getBody());
        verify(patientService, times(1)).updatePatient(cpf, patientDTO);
    }

    @Test
    void getPatientByCpfSuccessfully() {
        String cpf = "12345678900";
        PatientDTO patientDTO = new PatientDTO();
        when(patientService.getPatientByCpf(cpf)).thenReturn(patientDTO);

        ResponseEntity<PatientDTO> response = patientController.getPatientByCpf(cpf);

        assertEquals(patientDTO, response.getBody());
        verify(patientService, times(1)).getPatientByCpf(cpf);
    }

    @Test
    void listPatientByCpfSuccessfully() {
        String cpf = "12345678900";
        List<PatientDTO> patients = Collections.emptyList();
        when(patientService.listPatientsByCpf(cpf)).thenReturn(patients);

        ResponseEntity<List<PatientDTO>> response = patientController.listPatientByCpf(cpf);

        assertEquals(patients, response.getBody());
        verify(patientService, times(1)).listPatientsByCpf(cpf);
    }
}