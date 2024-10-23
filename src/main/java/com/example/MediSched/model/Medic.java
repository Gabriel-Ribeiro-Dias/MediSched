package com.example.MediSched.model;

import com.example.MediSched.exceptions.MedicScheduleNotDefinedException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity(name = "medic")
@Table(name = "tb_medic")
@Data
public class Medic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String expertise;
    @Column
    private String crm;
    @OneToOne(mappedBy = "medic")
    private User user;
    @OneToMany(mappedBy = "medic")
    private List<Appointment> appointment;
//  TODO: Uncomment the following code after finished the Schedule logic
//    @OneToMany(mappedBy = "medic")
//    private List<Schedule> schedules;

}
