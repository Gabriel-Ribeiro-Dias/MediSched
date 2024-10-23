package com.example.MediSched.model.dto;

import com.example.MediSched.model.Medic;
import com.example.MediSched.model.Patient;
import com.example.MediSched.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AppointmentDTO {
    private Long id;
    @Column
    @NotBlank(message = "Date is mandatory")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Date must be in the format dd/MM/yyyy")
    private String date;
    @Column
    @NotBlank(message = "Time is mandatory")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "Time must be in the format HH:mm")
    private String time;
    @ManyToOne
    @JoinColumn(name = "medic_id")
    private MedicDTO medic;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientDTO patient;
    @Column
    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
