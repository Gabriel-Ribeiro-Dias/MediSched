package com.example.MediSched.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MedicDTO {
    @Schema(description = "Medic's name", example = "Dr. House")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    @Schema(description = "Medic's expertise", example = "Cardiology")
    @NotBlank(message = "Expertise is mandatory")
    @Size(min = 2, max = 100, message = "Expertise must be between 2 and 100 characters")
    private String expertise;
    @Schema(description = "Medic's CRM", example = "12345")
    @NotBlank(message = "CRM is mandatory")
    @Size(min = 5, max = 20, message = "CRM must be between 5 and 20 characters")
    private String crm;
}
