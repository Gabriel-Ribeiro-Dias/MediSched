package com.example.MediSched.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity(name = "patient")
@Table(name = "tb_patient")
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String cpf;
    @Column
    private String email;
    @OneToOne(mappedBy = "patient")
    private User user;
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointment;
}
