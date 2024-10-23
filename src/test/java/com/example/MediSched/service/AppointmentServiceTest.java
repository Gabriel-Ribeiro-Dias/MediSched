package com.example.MediSched.service;

import com.example.MediSched.model.dto.AppointmentDTO;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.model.dto.PatientDTO;
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



}