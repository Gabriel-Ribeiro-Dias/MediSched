package com.example.MediSched.repository;

import com.example.MediSched.model.Appointment;
import com.example.MediSched.model.Medic;
import com.example.MediSched.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByMedicCrmAndDateAndTime(String crm, String date, String time);
    boolean existsByMedicCrmAndPatientCpfAndDateAndTime(String medicCrm, String patientCpf, String date, String time);
    List<Appointment> findByMedicCrm(String medicCrm);
    List<Appointment> findByPatientCpf(String patientCpf);
}
