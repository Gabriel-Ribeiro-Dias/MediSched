package com.example.MediSched.repository;

import com.example.MediSched.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByEmail(String email);

    Optional<Patient> findByCpf(String name);

    List<Patient> findAllByCpf(String cpf);
}
