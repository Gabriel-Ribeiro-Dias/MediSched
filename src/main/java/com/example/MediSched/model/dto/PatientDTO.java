package com.example.MediSched.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Data
public class PatientDTO {
    @Schema(description = "Patient's name", example = "John Doe")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    @Schema(description = "Patient's CPF", example = "12345678901")
    @NotBlank(message = "CPF is mandatory")
    @Pattern(regexp = "^[0-9]{11}$", message = "CPF must have 11 numbers")
    private String cpf;
    @Schema(description = "Patient's email", example = "example@gmail.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    private String email;
}
