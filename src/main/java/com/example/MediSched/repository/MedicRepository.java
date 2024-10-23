package com.example.MediSched.repository;

import com.example.MediSched.model.Medic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicRepository extends JpaRepository<Medic, Long> {
    Optional<Medic> findByCrm(String crm);

    List<Medic> findAllByCrm(String crm);
}
