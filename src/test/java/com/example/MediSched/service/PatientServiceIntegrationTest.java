package com.example.MediSched.service;

import com.example.MediSched.exceptions.PatientAlreadyExistsException;
import com.example.MediSched.exceptions.PatientNotFoundException;
import com.example.MediSched.model.Patient;
import com.example.MediSched.model.User;
import com.example.MediSched.model.dto.PatientDTO;
import com.example.MediSched.repository.PatientRepository;
import com.example.MediSched.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PatientServiceIntegrationTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository; // Supondo que você tenha um UserRepository

    private User testUser;

    @BeforeEach
    void setUp() {
        // Configura um usuário de teste
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        userRepository.save(testUser);
    }

    @Test
    @Sql(statements = "DELETE FROM tb_patient WHERE cpf = '12345678901'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createPatientSuccessfully() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("Jane Doe");
        patientDTO.setCpf("12345678901");
        patientDTO.setEmail("jane.doe@example.com");

        Patient result = patientService.create(patientDTO, testUser);

        assertNotNull(result);
        assertEquals(patientDTO.getName(), result.getName());
        assertEquals(patientDTO.getCpf(), result.getCpf());
        assertEquals(patientDTO.getEmail(), result.getEmail());

        // Verifica se o paciente foi salvo no banco de dados
        Patient savedPatient = patientRepository.findByCpf(patientDTO.getCpf()).orElse(null);
        assertNotNull(savedPatient);
    }

    @Test
    @Sql(statements = "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '12345678901', 'john.doe@example.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createPatientThrowsExceptionWhenCpfExists() {
        // Tenta criar outro paciente com o mesmo CPF
        PatientDTO duplicatePatientDTO = new PatientDTO();
        duplicatePatientDTO.setName("Jane Smith");
        duplicatePatientDTO.setCpf("12345678901"); // CPF duplicado
        duplicatePatientDTO.setEmail("jane.smith@example.com");

        assertThrows(PatientAlreadyExistsException.class, () -> patientService.create(duplicatePatientDTO, testUser));
    }

    @Test
    @Sql(statements = "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '12345678901', 'john.doe@example.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM tb_patient WHERE cpf = '12345678901'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deletePatientSuccessfully() {
        // Deleta o paciente
        patientService.deletePatient("12345678901");

        // Verifica se o paciente foi deletado
        assertFalse(patientRepository.findByCpf("12345678901").isPresent());
    }

    @Test
    void deletePatientThrowsExceptionWhenCpfIsNull() {
        assertThrows(IllegalArgumentException.class, () -> patientService.deletePatient(null));
    }

    @Test
    @Sql(statements = "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '12345678901', 'john.doe@example.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM tb_patient WHERE cpf = '12345678901'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updatePatientSuccessfully() {
        // Atualiza o paciente
        PatientDTO updatedPatientDTO = new PatientDTO();
        updatedPatientDTO.setName("John Smith");
        updatedPatientDTO.setCpf("12345678901");
        updatedPatientDTO.setEmail("john.smith@example.com");

        patientService.updatePatient("12345678901", updatedPatientDTO);

        // Verifica se o paciente foi atualizado
        Patient updatedPatient = patientRepository.findByCpf(updatedPatientDTO.getCpf()).orElse(null);
        assertNotNull(updatedPatient);
        assertEquals("John Smith", updatedPatient.getName());
        assertEquals("john.smith@example.com", updatedPatient.getEmail());
    }

    @Test
    @Sql(statements = "DELETE FROM tb_patient WHERE cpf = 'nonexistentcpf'", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updatePatientThrowsExceptionWhenPatientNotFound() {
        PatientDTO updatedPatientDTO = new PatientDTO();
        updatedPatientDTO.setName("John Smith");
        updatedPatientDTO.setCpf("12345678901");
        updatedPatientDTO.setEmail("john.smith@example.com");

        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient("nonexistentcpf", updatedPatientDTO));
    }

    @Test
    @Sql(statements = "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '12345678901', 'john.doe@example.com')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM tb_patient WHERE cpf = '12345678901'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getPatientByCpfSuccessfully() {
        // Obtém o paciente pelo CPF
        PatientDTO result = patientService.getPatientByCpf("12345678901");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("12345678901", result.getCpf());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void getPatientByCpfThrowsExceptionWhenCpfIsNull() {
        assertThrows(IllegalArgumentException.class, () -> patientService.getPatientByCpf(null));
    }

    @Test
    @Sql(statements = {
        "INSERT INTO tb_patient (name, cpf, email) VALUES ('Jane Doe', '12345678901', 'jane.doe@example.com')",
        "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '10987654321', 'john.doe@example.com')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
        "DELETE FROM tb_patient WHERE cpf = '12345678901'",
        "DELETE FROM tb_patient WHERE cpf = '10987654321'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllPatientsSuccessfully() {
        // Obtém todos os pacientes
        List<PatientDTO> patients = patientService.getAllPatients();

        assertEquals(2, patients.size());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('Jane Doe', '12345678901', 'jane.doe@example.com')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '12345678901', 'john.doe@example.com')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_patient WHERE cpf = '12345678901'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void listPatientsByCpfSuccessfully() {
        // Obtém pacientes pelo CPF
        List<PatientDTO> patients = patientService.listPatientsByCpf("12345678901");

        assertEquals(2, patients.size());
        assertEquals("Jane Doe", patients.get(0).getName());
        assertEquals("John Doe", patients.get(1).getName());
    }

    @Test
    void listPatientsByCpfThrowsExceptionWhenCpfIsNull() {
        assertThrows(IllegalArgumentException.class, () -> patientService.listPatientsByCpf(null));
    }
}
