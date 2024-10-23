package com.example.MediSched.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MedicDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    @NotBlank(message = "Expertise is mandatory")
    @Size(min = 2, max = 100, message = "Expertise must be between 2 and 100 characters")
    private String expertise;
    @NotBlank(message = "CRM is mandatory")
    @Size(min = 5, max = 20, message = "CRM must be between 5 and 20 characters")
    private String crm;
    private UserDTO user;
    private List<AppointmentDTO> appointmentList;
}
