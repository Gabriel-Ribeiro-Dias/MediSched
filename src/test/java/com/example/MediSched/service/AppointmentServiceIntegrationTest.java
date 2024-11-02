package com.example.MediSched.service;

import com.example.MediSched.exceptions.*;
import com.example.MediSched.model.Appointment;
import com.example.MediSched.model.dto.AppointmentDTO;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.model.dto.PatientDTO;
import com.example.MediSched.model.enums.AppointmentStatus;
import com.example.MediSched.repository.AppointmentRepository;
import com.example.MediSched.repository.MedicRepository;
import com.example.MediSched.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AppointmentServiceIntegrationTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private MedicRepository medicRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '123456789', 'john@example.com')",
            "INSERT INTO tb_appointment (date, time, medic_id, patient_id, status) " +
                    "VALUES ('01/10/2023', '10:00', (SELECT id FROM tb_medic WHERE crm = '12345'), " +
                    "(SELECT id FROM tb_patient WHERE cpf = '123456789'), 'SCHEDULED')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE medic_id = (SELECT id FROM tb_medic WHERE crm = '12345')",
            "DELETE FROM tb_medic WHERE crm = '12345'",
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void scheduleAppointment_shouldScheduleAppointment_whenValidData() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.getMedic().setName("Dr. Smith");
        appointmentDTO.getMedic().setCrm("12345");
        appointmentDTO.getMedic().setExpertise("Cardiology");

        appointmentDTO.setPatient(new PatientDTO());
        appointmentDTO.getPatient().setName("John Doe");
        appointmentDTO.getPatient().setEmail("john@example.com");
        appointmentDTO.getPatient().setCpf("123456789");

        appointmentDTO.setDate("01/10/2023");
        appointmentDTO.setTime("10:00");
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

        assertThrows(MedicNotAvailableException.class, () -> appointmentService.scheduleAppointment(appointmentDTO));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '123456789', 'john@example.com')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE medic_id = (SELECT id FROM tb_medic WHERE crm = '12345')",
            "DELETE FROM tb_medic WHERE crm = '12345'",
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void scheduleAppointment_shouldThrowException_whenMedicNotFound() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.setPatient(new PatientDTO());
        appointmentDTO.getPatient().setCpf("123456789");
        appointmentDTO.getMedic().setName("Dr. Smith");
        appointmentDTO.getMedic().setCrm("54321");
        appointmentDTO.getMedic().setExpertise("Cardiology");
        appointmentDTO.setDate("01/10/2023");
        appointmentDTO.setTime("10:00");
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

        assertThrows(MedicNotFoundException.class, () -> appointmentService.scheduleAppointment(appointmentDTO));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_medic WHERE crm = '12345'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void scheduleAppointment_shouldThrowException_whenPatientNotFound() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.getMedic().setName("Dr. Smith");
        appointmentDTO.getMedic().setCrm("12345");
        appointmentDTO.getMedic().setExpertise("Cardiology");

        appointmentDTO.setPatient(new PatientDTO());
        appointmentDTO.getPatient().setName("John Doe");
        appointmentDTO.getPatient().setEmail("john@example.com");
        appointmentDTO.getPatient().setCpf("123456789");

        appointmentDTO.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        appointmentDTO.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

        assertThrows(PatientNotFoundException.class, () -> appointmentService.scheduleAppointment(appointmentDTO));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '123456789', 'john@example.com')",
            "INSERT INTO tb_appointment (date, time, medic_id, patient_id, status) " +
                    "VALUES ('01/10/2023', '10:00'," +
                    "(SELECT id FROM tb_medic WHERE crm = '12345'),  " +
                    "(SELECT id FROM tb_patient WHERE cpf = '123456789')," +
                    "'SCHEDULED')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE medic_id = (SELECT id FROM tb_medic WHERE crm = '12345')",
            "DELETE FROM tb_medic WHERE crm = '12345'",
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void scheduleAppointment_shouldThrowException_whenMedicNotAvailable() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.getMedic().setName("Dr. Smith");
        appointmentDTO.getMedic().setCrm("12345");
        appointmentDTO.getMedic().setExpertise("Cardiology");

        appointmentDTO.setPatient(new PatientDTO());
        appointmentDTO.getPatient().setName("John Doe");
        appointmentDTO.getPatient().setEmail("john@example.com");
        appointmentDTO.getPatient().setCpf("123456789");

        appointmentDTO.setDate("01/10/2023");
        appointmentDTO.setTime("10:00");
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

        assertThrows(MedicNotAvailableException.class, () -> appointmentService.scheduleAppointment(appointmentDTO));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '123456789', 'john@example.com')",
            "INSERT INTO tb_appointment (id, date, time, medic_id, patient_id, status) " +
                    "VALUES (1, '01/10/2023', '10:00'," +
                    "(SELECT id FROM tb_medic WHERE crm = '12345'),  " +
                    "(SELECT id FROM tb_patient WHERE cpf = '123456789')," +
                    "'SCHEDULED')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE id = 1",
            "DELETE FROM tb_medic WHERE crm = '12345'",
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void cancelAppointment_shouldCancelAppointment_whenAppointmentExists() {
        appointmentService.cancelAppointment(1L);

        Appointment canceledAppointment = appointmentRepository.findById(1L).orElse(null);

        assertNotNull(canceledAppointment);
        assertEquals(AppointmentStatus.CANCELED, canceledAppointment.getStatus());
}

    @Test
    void cancelAppointment_shouldThrowException_whenAppointmentNotFound() {
        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.cancelAppointment(999L));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '123456789', 'john@example.com')",
            "INSERT INTO tb_appointment (id, date, time, medic_id, patient_id, status) " +
                    "VALUES (1, '01/10/2023', '10:00'," +
                    "(SELECT id FROM tb_medic WHERE crm = '12345'),  " +
                    "(SELECT id FROM tb_patient WHERE cpf = '123456789')," +
                    "'SCHEDULED')",
            "INSERT INTO tb_appointment (id, date, time, medic_id, patient_id, status) " +
                    "VALUES (2, '01/10/2023', '11:00'," +
                    "(SELECT id FROM tb_medic WHERE crm = '12345'),  " +
                    "(SELECT id FROM tb_patient WHERE cpf = '123456789')," +
                    "'SCHEDULED')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE medic_id = (SELECT id FROM tb_medic WHERE crm = '12345')",
            "DELETE FROM tb_medic WHERE crm = '12345'",
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAppointments_shouldReturnListOfAppointments() {
        List<AppointmentDTO> result = appointmentService.getAppointments();

        assertEquals(2, result.size());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '123456789', 'john@example.com')",
            "INSERT INTO tb_appointment (id, date, time, medic_id, patient_id, status) " +
                    "VALUES (1, '01/10/2023', '10:00'," +
                    "(SELECT id FROM tb_medic WHERE crm = '12345'),  " +
                    "(SELECT id FROM tb_patient WHERE cpf = '123456789')," +
                    "'SCHEDULED')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE id = 1",
            "DELETE FROM tb_medic WHERE crm = '12345'",
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAppointment_shouldReturnAppointment_whenAppointmentExists() {
        AppointmentDTO result = appointmentService.getAppointment(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE id = 1"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAppointment_shouldThrowException_whenAppointmentNotFound() {
        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getAppointment(1L));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '123456789', 'john@example.com')",
            "INSERT INTO tb_appointment (id, date, time, medic_id, patient_id, status) " +
            "VALUES (1, '01/10/2023', '10:00', " +
            "(SELECT id FROM tb_medic WHERE crm = '12345'), " +
            "(SELECT id FROM tb_patient WHERE cpf = '123456789'), 'SCHEDULED')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE id = 1",
            "DELETE FROM tb_medic WHERE crm = '12345'",
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAppointmentsByMedic_shouldReturnListOfAppointments_whenMedicExists() {
        List<AppointmentDTO> result = appointmentService.getAppointmentsByMedic("12345");

        assertEquals(1, result.size());
    }

    @Test
    @Sql(statements = {
            "DELETE FROM tb_medic WHERE crm = '12345'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAppointmentsByMedic_shouldThrowException_whenMedicNotFound() {
        assertThrows(MedicNotFoundException.class, () -> appointmentService.getAppointmentsByMedic("12345"));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '123456789', 'john@example.com')",
            "INSERT INTO tb_appointment (id, date, time, medic_id, patient_id, status) " +
            "VALUES (1, '01/10/2023', '10:00', " +
            "(SELECT id FROM tb_medic WHERE crm = '12345'), " +
            "(SELECT id FROM tb_patient WHERE cpf = '123456789'), 'SCHEDULED')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE id = 1",
            "DELETE FROM tb_medic WHERE crm = '12345'",
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAppointmentsByPatient_shouldReturnListOfAppointments_whenPatientExists() {
        List<AppointmentDTO> result = appointmentService.getAppointmentsByPatient("123456789");

        assertEquals(1, result.size());
    }

    @Test
    @Sql(statements = {
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAppointmentsByPatient_shouldThrowException_whenPatientNotFound() {
        assertThrows(PatientNotFoundException.class, () -> appointmentService.getAppointmentsByPatient("123456789"));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Smith', '12345', 'Cardiology')",
            "INSERT INTO tb_patient (name, cpf, email) VALUES ('John Doe', '123456789', 'john@example.com')",
            "INSERT INTO tb_appointment (id, date, time, medic_id, patient_id, status) " +
            "VALUES (1, '01/10/2023', '10:00', " +
            "(SELECT id FROM tb_medic WHERE crm = '12345'), " +
            "(SELECT id FROM tb_patient WHERE cpf = '123456789'), 'SCHEDULED')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE id = 1",
            "DELETE FROM tb_medic WHERE crm = '12345'",
            "DELETE FROM tb_patient WHERE cpf = '123456789'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAppointment_shouldUpdateAppointment_whenAppointmentExists() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDate("02/10/2023");
        appointmentDTO.setTime("11:00");
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.getMedic().setCrm("12345");
        appointmentDTO.setPatient(new PatientDTO());
        appointmentDTO.getPatient().setCpf("123456789");
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

        Appointment result = appointmentService.updateAppointment(1L, appointmentDTO);

        assertNotNull(result);
        assertEquals("02/10/2023", result.getDate());
        assertEquals("11:00", result.getTime());
        assertEquals("12345", result.getMedic().getCrm());
        assertEquals("123456789", result.getPatient().getCpf());
    }

    @Test
    @Sql(statements = {
            "DELETE FROM tb_appointment WHERE id = 1"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAppointment_shouldThrowException_whenAppointmentNotFound() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDate("01/10/2023");
        appointmentDTO.setTime("10:00");
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.getMedic().setCrm("12345");
        appointmentDTO.setPatient(new PatientDTO());
        appointmentDTO.getPatient().setCpf("123456789");
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.updateAppointment(1L, appointmentDTO));
    }
}