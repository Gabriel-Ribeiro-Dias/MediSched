package com.example.MediSched.model.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MedicDTOTest {

    private Validator validator;

    @BeforeAll
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidMedicDTO() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("Dr. House");
        medicDTO.setExpertise("Cardiology");
        medicDTO.setCrm("12345");

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid MedicDTO");
    }

    @Test
    void testNameBlank() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName(""); // Nome em branco
        medicDTO.setExpertise("Cardiology");
        medicDTO.setCrm("12345");

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a blank name");
    }

    @Test
    void testNameTooShort() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("A"); // Nome muito curto
        medicDTO.setExpertise("Cardiology");
        medicDTO.setCrm("12345");

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a name that is too short");
    }

    @Test
    void testNameTooLong() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("A".repeat(101)); // Nome muito longo
        medicDTO.setExpertise("Cardiology");
        medicDTO.setCrm("12345");

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a name that is too long");
    }

    @Test
    void testExpertiseBlank() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("Dr. House");
        medicDTO.setExpertise(""); // Expertise em branco
        medicDTO.setCrm("12345");

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a blank expertise");
    }

    @Test
    void testExpertiseTooShort() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("Dr. House");
        medicDTO.setExpertise("A"); // Expertise muito curta
        medicDTO.setCrm("12345");

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for an expertise that is too short");
    }

    @Test
    void testExpertiseTooLong() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("Dr. House");
        medicDTO.setExpertise("A".repeat(101)); // Expertise muito longa
        medicDTO.setCrm("12345");

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for an expertise that is too long");
    }

    @Test
    void testCrmBlank() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("Dr. House");
        medicDTO.setExpertise("Cardiology");
        medicDTO.setCrm(""); // CRM em branco

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a blank CRM");
    }

    @Test
    void testCrmTooShort() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("Dr. House");
        medicDTO.setExpertise("Cardiology");
        medicDTO.setCrm("1234"); // CRM muito curto

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a CRM that is too short");
    }

    @Test
    void testCrmTooLong() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("Dr. House");
        medicDTO.setExpertise("Cardiology");
        medicDTO.setCrm("1".repeat(21)); // CRM muito longo

        Set<ConstraintViolation<MedicDTO>> violations = validator.validate(medicDTO);
        assertFalse(violations.isEmpty(), "Expected validation errors for a CRM that is too long");
    }
}