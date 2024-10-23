package com.example.MediSched.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity(name = "schedule")
@Table(name = "tb_schedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotNull(message = "Date is mandatory")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Date must be in the format dd/MM/yyyy")
    private String date;
    @Column
    @NotNull(message = "Times are mandatory")
    @Size(min = 1, message = "At least one time must be informed")
    private List<String> times;
    @ManyToOne
    @JoinColumn(name = "medic_id")
    private Medic medic;

    public Schedule(String date, List<String> time) {
        this.date = date;
        this.times = time;
    }
}
