package com.example.MediSched.model.dto;

import com.example.MediSched.model.Medic;
import com.example.MediSched.model.Patient;
import com.example.MediSched.model.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AppointmentDTO {
    @Schema(hidden = true)
    private Long id;
    @Schema(description = "Appointment's date", example = "01/01/2022")
    @Column
    @NotBlank(message = "Date is mandatory")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Date must be in the format dd/MM/yyyy")
    private String date;
    @Schema(description = "Appointment's time", example = "08:00")
    @Column
    @NotBlank(message = "Time is mandatory")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "Time must be in the format HH:mm")
    private String time;
    @Schema(description = "Appointment's medic data")
    private MedicDTO medic;
    @Schema(description = "Appointment's patient data")
    private PatientDTO patient;
    @Schema(description = "Appointment's status", example = "SCHEDULED")
    @Column
    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
