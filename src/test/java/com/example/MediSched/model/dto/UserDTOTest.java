package com.example.MediSched.model.dto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {
    private Validator validator;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        userDTO = new UserDTO();
    }

    @Test
    void testUsernameValidation() {
        userDTO.setUsername("");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPasswordValidation() {
        userDTO.setPassword("123");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidUserDTO() {
        userDTO.setUsername("johndoe");
        userDTO.setPassword("password123");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertTrue(violations.isEmpty());
    }
}