package com.example.MediSched.repository;

import com.example.MediSched.model.Appointment;
import com.example.MediSched.model.Medic;
import com.example.MediSched.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private MedicRepository medicRepository;
    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        Medic medic1 = new Medic();
        medic1.setCrm("12345");
        medicRepository.save(medic1);

        Patient patient1 = new Patient();
        patient1.setCpf("11111111111");
        patientRepository.save(patient1);

        Medic medic2 = new Medic();
        medic2.setCrm("12346");
        medicRepository.save(medic2);

        Patient patient2 = new Patient();
        patient2.setCpf("22222222222");
        patientRepository.save(patient2);

        Appointment appointment1 = new Appointment();
        appointment1.setDate("2023-10-01");
        appointment1.setTime("10:00");
        appointment1.setMedic(medic1);
        appointment1.setPatient(patient1);
        appointmentRepository.save(appointment1);

        Appointment appointment2 = new Appointment();
        appointment2.setDate("2023-10-01");
        appointment2.setTime("11:00");
        appointment2.setMedic(medic2);
        appointment2.setPatient(patient2);
        appointmentRepository.save(appointment2);
    }

    @Test
    void existsByMedicCrmAndDateAndTime_returnsTrueWhenExists() {
        boolean exists = appointmentRepository.existsByMedicCrmAndDateAndTime(
                "12345", "2023-10-01", "10:00");
        assertTrue(exists);
    }

    @Test
    void existsByMedicCrmAndDateAndTime_returnsFalseWhenNotExists() {
        boolean exists = appointmentRepository.existsByMedicCrmAndDateAndTime(
                "12345", "2023-10-01", "12:00");
        assertFalse(exists);
    }

    @Test
    void existsByMedicCrmAndPatientCpfAndDateAndTime_returnsTrueWhenExists() {
        boolean exists = appointmentRepository.existsByMedicCrmAndPatientCpfAndDateAndTime(
                "12345", "11111111111", "2023-10-01", "10:00");
        assertTrue(exists);
    }

    @Test
    void existsByMedicCrmAndPatientCpfAndDateAndTime_returnsFalseWhenNotExists() {
        boolean exists = appointmentRepository.existsByMedicCrmAndPatientCpfAndDateAndTime(
                "12345", "11111111111", "2023-10-01", "12:00");
        assertFalse(exists);
    }

    @Test
    void findByMedicCrm_returnsAppointmentsForMedic() {
        List<Appointment> appointments = appointmentRepository.findByMedicCrm("12345");
        assertEquals(1, appointments.size());
    }

    @Test
    void findByPatientCpf_returnsAppointmentsForPatient() {
        List<Appointment> appointments = appointmentRepository.findByPatientCpf("11111111111");
        assertEquals(1, appointments.size());
    }
}