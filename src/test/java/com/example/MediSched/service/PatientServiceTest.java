package com.example.MediSched.service;

import com.example.MediSched.exceptions.PatientAlreadyExistsException;
import com.example.MediSched.exceptions.PatientNotFoundException;
import com.example.MediSched.model.Patient;
import com.example.MediSched.model.User;
import com.example.MediSched.model.dto.PatientDTO;
import com.example.MediSched.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void create_withValidPatientDTO_createsPatientSuccessfully() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setEmail("john@example.com");
        patientDTO.setCpf("123456789");
        User user = new User();

        when(patientRepository.findByCpf("123456789")).thenReturn(Optional.empty());

        patientService.create(patientDTO, user);

        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void create_withExistingCpf_throwsPatientAlreadyExistsException() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setEmail("john@example.com");
        patientDTO.setCpf("123456789");
        User user = new User();

        when(patientRepository.findByCpf("123456789")).thenReturn(Optional.of(new Patient()));

        assertThrows(PatientAlreadyExistsException.class, () -> patientService.create(patientDTO, user));
    }

    @Test
    void deletePatient_withValidCpf_deletesPatientSuccessfully() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setCpf("123456789");

        when(patientRepository.findByCpf("123456789")).thenReturn(Optional.of(patient));

        patientService.deletePatient("123456789");

        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePatient_withInvalidCpf_throwsPatientNotFoundException() {
        when(patientRepository.findByCpf("invalid")).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient("invalid"));
    }

    @Test
    void updatePatient_withValidCpf_updatesPatientSuccessfully() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setEmail("john@example.com");
        patientDTO.setCpf("123456789");
        Patient patient = new Patient();
        patient.setCpf("123456789");

        when(patientRepository.findByCpf("123456789")).thenReturn(Optional.of(patient));

        patientService.updatePatient("123456789", patientDTO);

        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_withInvalidCpf_throwsPatientNotFoundException() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setEmail("john@example.com");
        patientDTO.setCpf("123456789");

        when(patientRepository.findByCpf("invalid")).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient("invalid", patientDTO));
    }

    @Test
    void getPatientByCpf_withValidCpf_returnsPatientDTO() {
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setCpf("123456789");

        when(patientRepository.findByCpf("123456789")).thenReturn(Optional.of(patient));

        PatientDTO result = patientService.getPatientByCpf("123456789");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("123456789", result.getCpf());
    }

    @Test
    void getPatientByCpf_withInvalidCpf_throwsPatientNotFoundException() {
        when(patientRepository.findByCpf("invalid")).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientByCpf("invalid"));
    }

    @Test
    void getAllPatients_returnsListOfPatientDTOs() {
        List<Patient> patients = new ArrayList<>();
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setCpf("123456789");
        patients.add(patient);

        when(patientRepository.findAll()).thenReturn(patients);

        List<PatientDTO> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("123456789", result.get(0).getCpf());
    }

    @Test
    void listPatientsByCpf_withValidCpf_returnsListOfPatientDTOs() {
        List<Patient> patients = new ArrayList<>();
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setCpf("123456789");
        patients.add(patient);

        when(patientRepository.findAllByCpf("123456789")).thenReturn(patients);

        List<PatientDTO> result = patientService.listPatientsByCpf("123456789");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("123456789", result.get(0).getCpf());
    }

    @Test
    void listPatientsByCpf_withInvalidCpf_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> patientService.listPatientsByCpf(null));
        assertThrows(IllegalArgumentException.class, () -> patientService.listPatientsByCpf(""));
    }
}