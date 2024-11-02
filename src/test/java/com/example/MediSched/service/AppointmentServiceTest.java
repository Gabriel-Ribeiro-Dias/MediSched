package com.example.MediSched.service;

import com.example.MediSched.model.dto.AppointmentDTO;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.model.dto.PatientDTO;
import com.example.MediSched.model.enums.AppointmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import com.example.MediSched.repository.AppointmentRepository;
import com.example.MediSched.repository.MedicRepository;
import com.example.MediSched.repository.PatientRepository;
import com.example.MediSched.model.Medic;
import com.example.MediSched.model.Patient;
import com.example.MediSched.model.Appointment;
import com.example.MediSched.exceptions.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private MedicRepository medicRepository;
    @Mock
    private PatientRepository patientRepository;
    @InjectMocks
    private AppointmentService appointmentService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
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

        appointmentDTO.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        appointmentDTO.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        when(medicRepository.findByCrm(anyString())).thenReturn(Optional.of(new Medic()));
        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.of(new Patient()));
        when(appointmentRepository.existsByMedicCrmAndDateAndTime(anyString(), any(), any())).thenReturn(false);
        when(appointmentRepository.existsByMedicCrmAndPatientCpfAndDateAndTime(anyString(), anyString(), any(), any())).thenReturn(false);

        appointmentService.scheduleAppointment(appointmentDTO);

        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void scheduleAppointment_shouldThrowException_whenMedicNotFound() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.getMedic().setName("Dr. Smith");
        appointmentDTO.getMedic().setCrm("12345");
        appointmentDTO.getMedic().setExpertise("Cardiology");
        appointmentDTO.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        appointmentDTO.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        when(medicRepository.findByCrm(anyString())).thenReturn(Optional.empty());

        assertThrows(MedicNotFoundException.class, () -> appointmentService.scheduleAppointment(appointmentDTO));
    }

    @Test
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

        when(medicRepository.findByCrm(anyString())).thenReturn(Optional.of(new Medic()));
        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> appointmentService.scheduleAppointment(appointmentDTO));
    }

    @Test
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

        appointmentDTO.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        appointmentDTO.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        when(medicRepository.findByCrm(anyString())).thenReturn(Optional.of(new Medic()));
        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.of(new Patient()));
        when(appointmentRepository.existsByMedicCrmAndDateAndTime(anyString(), any(), any())).thenReturn(true);

        assertThrows(MedicNotAvailableException.class, () -> appointmentService.scheduleAppointment(appointmentDTO));
    }

    @Test
    void scheduleAppointment_shouldThrowException_whenAppointmentAlreadyScheduled() {
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

        when(medicRepository.findByCrm(anyString())).thenReturn(Optional.of(new Medic()));
        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.of(new Patient()));
        when(appointmentRepository.existsByMedicCrmAndDateAndTime(anyString(), any(), any())).thenReturn(false);
        when(appointmentRepository.existsByMedicCrmAndPatientCpfAndDateAndTime(anyString(), anyString(), any(), any())).thenReturn(true);

        assertThrows(AppointmentAlreadyScheduledException.class, () -> appointmentService.scheduleAppointment(appointmentDTO));
    }

    @Test
    void cancelAppointment_shouldCancelAppointment_whenAppointmentExists() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        appointmentService.cancelAppointment(appointmentId);

        assertEquals(AppointmentStatus.CANCELED, appointment.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void cancelAppointment_shouldThrowException_whenAppointmentNotFound() {
        Long appointmentId = 1L;

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.cancelAppointment(appointmentId));
    }

    @Test
    void getAppointmentsByMedic_shouldReturnAppointments_whenMedicExists() {
        String medicCrm = "12345";
        Medic medic = new Medic();
        Patient patient = new Patient();
        medic.setCrm(medicCrm);

        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointment.setMedic(medic);
        appointment.setPatient(patient);
        appointments.add(appointment);

        when(medicRepository.findByCrm(medicCrm)).thenReturn(Optional.of(medic));
        when(appointmentRepository.findByMedicCrm(medicCrm)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentService.getAppointmentsByMedic(medicCrm);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicRepository, times(1)).findByCrm(medicCrm);
        verify(appointmentRepository, times(1)).findByMedicCrm(medicCrm);
    }

    @Test
    void getAppointmentsByMedic_shouldThrowException_whenMedicNotFound() {
        String medicCrm = "12345";

        when(medicRepository.findByCrm(medicCrm)).thenReturn(Optional.empty());

        assertThrows(MedicNotFoundException.class, () -> appointmentService.getAppointmentsByMedic(medicCrm));
        verify(medicRepository, times(1)).findByCrm(medicCrm);
        verify(appointmentRepository, times(0)).findByMedicCrm(medicCrm);
    }

    @Test
    void getAppointment_shouldReturnAppointment_whenAppointmentExists() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setDate("01/01/2023");
        appointment.setTime("10:00");
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        Medic medic = new Medic();
        medic.setName("Dr. Smith");
        medic.setCrm("12345");
        medic.setExpertise("Cardiology");
        appointment.setMedic(medic);
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setCpf("123456789");
        appointment.setPatient(patient);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        AppointmentDTO result = appointmentService.getAppointment(appointmentId);

        assertNotNull(result);
        assertEquals(appointmentId, result.getId());
        verify(appointmentRepository, times(1)).findById(appointmentId);
    }

    @Test
    void getAppointment_shouldThrowException_whenAppointmentNotFound() {
        Long appointmentId = 1L;

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getAppointment(appointmentId));
        verify(appointmentRepository, times(1)).findById(appointmentId);
    }

    @Test
    void getAppointmentsByPatient_shouldReturnAppointments_whenPatientExists() {
        String patientCpf = "123456789";
        Patient patient = new Patient();
        patient.setCpf(patientCpf);
        Medic medic = new Medic();

        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointment.setMedic(medic);
        appointment.setPatient(patient);
        appointments.add(appointment);

        when(patientRepository.findByCpf(patientCpf)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientCpf(patientCpf)).thenReturn(appointments);

        List<AppointmentDTO> result = appointmentService.getAppointmentsByPatient(patientCpf);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(patientRepository, times(1)).findByCpf(patientCpf);
        verify(appointmentRepository, times(1)).findByPatientCpf(patientCpf);
    }

    @Test
    void getAppointmentsByPatient_shouldReturnEmptyList_whenNoAppointments() {
        String patientCpf = "123456789";
        Patient patient = new Patient();
        patient.setCpf(patientCpf);

        when(patientRepository.findByCpf(patientCpf)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientCpf(patientCpf)).thenReturn(new ArrayList<>());

        List<AppointmentDTO> result = appointmentService.getAppointmentsByPatient(patientCpf);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(patientRepository, times(1)).findByCpf(patientCpf);
        verify(appointmentRepository, times(1)).findByPatientCpf(patientCpf);
    }

    @Test
    void getAppointmentsByPatient_shouldThrowException_whenPatientNotFound() {
        String patientCpf = "123456789";

        when(patientRepository.findByCpf(patientCpf)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> appointmentService.getAppointmentsByPatient(patientCpf));
        verify(patientRepository, times(1)).findByCpf(patientCpf);
        verify(appointmentRepository, times(0)).findByPatientCpf(patientCpf);
    }

    @Test
    void updateAppointment_shouldUpdateAppointment_whenValidData() {
        Long appointmentId = 1L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.getMedic().setName("Dr. Smith");
        appointmentDTO.getMedic().setCrm("12345");
        appointmentDTO.getMedic().setExpertise("Cardiology");

        appointmentDTO.setPatient(new PatientDTO());
        appointmentDTO.getPatient().setName("John Doe");
        appointmentDTO.getPatient().setEmail("john@example.com");
        appointmentDTO.getPatient().setCpf("123456789");

        appointmentDTO.setDate("01/01/2023");
        appointmentDTO.setTime("10:00");
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setMedic(new Medic());
        appointment.setPatient(new Patient());

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.existsByMedicCrmAndDateAndTime(anyString(), any(), any())).thenReturn(false);
        when(medicRepository.findByCrm(anyString())).thenReturn(Optional.of(new Medic()));
        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.of(new Patient()));

        Appointment updatedAppointment = appointmentService.updateAppointment(appointmentId, appointmentDTO);

        assertNotNull(updatedAppointment, "The updated appointment should not be null");
        assertEquals(appointmentDTO.getDate(), updatedAppointment.getDate());
        assertEquals(appointmentDTO.getTime(), updatedAppointment.getTime());
        assertEquals(appointmentDTO.getStatus(), updatedAppointment.getStatus());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void updateAppointment_shouldThrowException_whenAppointmentNotFound() {
        Long appointmentId = 1L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.updateAppointment(appointmentId, appointmentDTO));
    }

    @Test
    void updateAppointment_shouldThrowException_whenMedicNotAvailable() {
        Long appointmentId = 1L;
        String date = "01/01/2022";
        String time = "08:00";
        String medicCrm = "12345";

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDate("01/01/2022");
        appointmentDTO.setTime("08:00");
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.getMedic().setCrm("12345");

        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(appointmentId);
        existingAppointment.setMedic(new Medic());
        existingAppointment.getMedic().setCrm(medicCrm);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentRepository.existsByMedicCrmAndDateAndTime(medicCrm, date, time)).thenReturn(true);

        assertThrows(MedicNotAvailableException.class, () ->
                appointmentService.updateAppointment(appointmentId, appointmentDTO)
        );

        verify(appointmentRepository).existsByMedicCrmAndDateAndTime(medicCrm, date, time);
    }



}