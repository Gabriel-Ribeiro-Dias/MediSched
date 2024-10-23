package com.example.MediSched.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Data
public class PatientDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    @NotBlank(message = "CPF is mandatory")
    @Pattern(regexp = "^[0-9]{11}$", message = "CPF must have 11 numbers")
    private String cpf;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    private String email;
    private UserDTO user;
    private List<AppointmentDTO> appointmentList;
}
