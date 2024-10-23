package com.example.MediSched.model.dto;

import com.example.MediSched.model.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
    private MedicDTO medic;
    private PatientDTO patient;
}
