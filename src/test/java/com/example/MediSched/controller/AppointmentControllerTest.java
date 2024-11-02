package com.example.MediSched.controller;

import com.example.MediSched.model.dto.AppointmentDTO;
import com.example.MediSched.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void scheduleAppointment() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        doNothing().when(appointmentService).scheduleAppointment(appointmentDTO);

        ResponseEntity<String> response = appointmentController.scheduleAppointment(appointmentDTO);

        assertEquals("Appointment scheduled successfully", response.getBody());
        verify(appointmentService, times(1)).scheduleAppointment(appointmentDTO);
    }

    @Test
    void cancelAppointment() {
        Long appointmentId = 1L;
        doNothing().when(appointmentService).cancelAppointment(appointmentId);

        ResponseEntity<String> response = appointmentController.cancelAppointment(appointmentId);

        assertEquals("Appointment canceled successfully", response.getBody());
        verify(appointmentService, times(1)).cancelAppointment(appointmentId);
    }

    @Test
    void getAppointments() {
        List<AppointmentDTO> appointments = Collections.emptyList();
        when(appointmentService.getAppointments()).thenReturn(appointments);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointments();

        assertEquals(appointments, response.getBody());
        verify(appointmentService, times(1)).getAppointments();
    }

    @Test
    void getAppointment() {
        Long appointmentId = 1L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        when(appointmentService.getAppointment(appointmentId)).thenReturn(appointmentDTO);

        ResponseEntity<AppointmentDTO> response = appointmentController.getAppointment(appointmentId);

        assertEquals(appointmentDTO, response.getBody());
        verify(appointmentService, times(1)).getAppointment(appointmentId);
    }

    @Test
    void listAppointmentsByMedic() {
        String medicCrm = "12345";
        List<AppointmentDTO> appointments = Collections.emptyList();
        when(appointmentService.getAppointmentsByMedic(medicCrm)).thenReturn(appointments);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.listAppointmentsByMedic(medicCrm);

        assertEquals(appointments, response.getBody());
        verify(appointmentService, times(1)).getAppointmentsByMedic(medicCrm);
    }

    @Test
    void listAppointmentsByPatient() {
        String patientCpf = "12345678900";
        List<AppointmentDTO> appointments = Collections.emptyList();
        when(appointmentService.getAppointmentsByPatient(patientCpf)).thenReturn(appointments);

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.listAppointmentsByPatient(patientCpf);

        assertEquals(appointments, response.getBody());
        verify(appointmentService, times(1)).getAppointmentsByPatient(patientCpf);
    }

    @Test
    void updateAppointment() {
        Long appointmentId = 1L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        when(appointmentService.updateAppointment(appointmentId, appointmentDTO)).thenReturn(null);

        ResponseEntity<String> response = appointmentController.updateAppointment(appointmentId, appointmentDTO);

        assertEquals("Appointment updated successfully", response.getBody());
        verify(appointmentService, times(1)).updateAppointment(appointmentId, appointmentDTO);
    }
}