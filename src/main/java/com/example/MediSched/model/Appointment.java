package com.example.MediSched.model;

import com.example.MediSched.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "appointment")
@Table(name = "tb_appointment")
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String date;
    @Column
    private String time;
    @ManyToOne
    @JoinColumn(name = "medic_id")
    private Medic medic;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @Column
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
