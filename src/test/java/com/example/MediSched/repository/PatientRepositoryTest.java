package com.example.MediSched.repository;

import com.example.MediSched.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        Patient patient1 = new Patient();
        patient1.setEmail("john.doe@example.com");
        patient1.setCpf("11111111111");

        Patient patient2 = new Patient();
        patient2.setEmail("jane.doe@example.com");
        patient2.setCpf("22222222222");

        patientRepository.save(patient1);
        patientRepository.save(patient2);
    }

    @Test
    void findByEmail_returnsPatientWhenExists() {
        Patient patient = patientRepository.findByEmail("john.doe@example.com");
        assertNotNull(patient);
        assertEquals("11111111111", patient.getCpf());
    }

    @Test
    void findByEmail_returnsNullWhenNotExists() {
        Patient patient = patientRepository.findByEmail("nonexistent@example.com");
        assertNull(patient);
    }

    @Test
    void findByCpf_returnsPatientWhenExists() {
        Optional<Patient> patient = patientRepository.findByCpf("11111111111");
        assertTrue(patient.isPresent());
        assertEquals("john.doe@example.com", patient.get().getEmail());
    }

    @Test
    void findByCpf_returnsEmptyWhenNotExists() {
        Optional<Patient> patient = patientRepository.findByCpf("00000000000");
        assertFalse(patient.isPresent());
    }

    @Test
    void findAllByCpf_returnsListOfPatientsWhenExists() {
        List<Patient> patients = patientRepository.findAllByCpf("11111111111");
        assertEquals(1, patients.size());
        assertEquals("john.doe@example.com", patients.get(0).getEmail());
    }

    @Test
    void findAllByCpf_returnsEmptyListWhenNotExists() {
        List<Patient> patients = patientRepository.findAllByCpf("00000000000");
        assertTrue(patients.isEmpty());
    }
}