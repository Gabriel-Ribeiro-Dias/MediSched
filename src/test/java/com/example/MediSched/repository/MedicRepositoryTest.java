package com.example.MediSched.repository;

import com.example.MediSched.model.Medic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MedicRepositoryTest {

    @Autowired
    private MedicRepository medicRepository;

    @BeforeEach
    void setUp() {
        Medic medic1 = new Medic();
        medic1.setCrm("12346");
        medic1.setName("Dr. John Doe");

        Medic medic2 = new Medic();
        medic2.setCrm("67890");
        medic2.setName("Dr. Jane Smith");

        medicRepository.save(medic1);
        medicRepository.save(medic2);
    }

    @Test
    void findByCrm_returnsMedicWhenExists() {
        Optional<Medic> medic = medicRepository.findByCrm("12346");
        assertTrue(medic.isPresent());
        assertEquals("Dr. John Doe", medic.get().getName());
    }

    @Test
    void findByCrm_returnsEmptyWhenNotExists() {
        Optional<Medic> medic = medicRepository.findByCrm("00000");
        assertFalse(medic.isPresent());
    }

    @Test
    void findAllByCrm_returnsListOfMedicsWhenExists() {
        List<Medic> medics = medicRepository.findAllByCrm("12346");
        assertEquals(1, medics.size());
        assertEquals("Dr. John Doe", medics.get(0).getName());
    }

    @Test
    void findAllByCrm_returnsEmptyListWhenNotExists() {
        List<Medic> medics = medicRepository.findAllByCrm("00000");
        assertTrue(medics.isEmpty());
    }
}