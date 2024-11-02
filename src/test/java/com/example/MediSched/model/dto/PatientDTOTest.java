package com.example.MediSched.model.dto;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PatientDTOTest {

    private Validator validator;

    @BeforeAll
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidPatientDTO() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setCpf("12345678901");
        patientDTO.setEmail("example@gmail.com");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);
        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid PatientDTO");
    }

    @Test
    void testNameBlank() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName(""); // Nome em branco
        patientDTO.setCpf("12345678901");
        patientDTO.setEmail("example@gmail.com");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a blank name");
    }

    @Test
    void testNameTooLong() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("A".repeat(101)); // Nome com 101 caracteres
        patientDTO.setCpf("12345678901");
        patientDTO.setEmail("example@gmail.com");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a name that is too long");
    }

    @Test
    void testCpfBlank() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setCpf(""); // CPF em branco
        patientDTO.setEmail("example@gmail.com");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a blank CPF");
    }

    @Test
    void testCpfInvalidFormat() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setCpf("123456"); // CPF com formato inválido
        patientDTO.setEmail("example@gmail.com");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for an invalid CPF format");
    }

    @Test
    void testEmailBlank() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setCpf("12345678901");
        patientDTO.setEmail(""); // Email em branco

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a blank email");
    }

    @Test
    void testEmailInvalidFormat() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setCpf("12345678901");
        patientDTO.setEmail("invalid-email"); // Email com formato inválido

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for an invalid email format");
    }
}
