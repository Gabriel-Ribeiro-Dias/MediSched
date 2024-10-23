package com.example.MediSched.controller;

import com.example.MediSched.model.dto.AppointmentDTO;
import com.example.MediSched.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing appointments.
 * Provides endpoints for scheduling, canceling, updating, and retrieving appointments.
 */
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Constructor for AppointmentController.
     *
     * @param appointmentService the service to handle appointment operations
     */
    @Autowired
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    /**
     * Schedules a new appointment.
     *
     * @param appointmentDTO the appointment data transfer object containing appointment details
     * @return a response entity with a success message
     */
    @PostMapping
    public ResponseEntity<String> scheduleAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        appointmentService.scheduleAppointment(appointmentDTO);
        return ResponseEntity.ok().body("Appointment scheduled successfully");
    }

    /**
     * Cancels an appointment by ID.
     *
     * @param appointmentId the ID of the appointment to cancel
     * @return a response entity with a success message
     */
    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok().body("Appointment canceled successfully");
    }

    /**
     * Retrieves a list of all appointments.
     *
     * @return a response entity with a list of appointment data transfer objects
     */
    @GetMapping("/list")
    public ResponseEntity<List<AppointmentDTO>> getAppointments() {
        return ResponseEntity.ok().body(appointmentService.getAppointments());
    }

    /**
     * Retrieves an appointment by ID.
     *
     * @param appointmentId the ID of the appointment to retrieve
     * @return a response entity with the appointment data transfer object
     */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok().body(appointmentService.getAppointment(appointmentId));
    }

    /**
     * Lists appointments by medic CRM.
     *
     * @param medicCrm the CRM of the medic to filter appointments
     * @return a response entity with a list of appointment data transfer objects
     */
    @GetMapping("/list-by-medic")
    public ResponseEntity<List<AppointmentDTO>> listAppointmentsByMedic(@RequestParam String medicCrm) {
        return ResponseEntity.ok().body(appointmentService.getAppointmentsByMedic(medicCrm));
    }

    /**
     * Lists appointments by patient CPF.
     *
     * @param patientCpf the CPF of the patient to filter appointments
     * @return a response entity with a list of appointment data transfer objects
     */
    @GetMapping("/list-by-patient")
    public ResponseEntity<List<AppointmentDTO>> listAppointmentsByPatient(@RequestParam String patientCpf) {
        return ResponseEntity.ok().body(appointmentService.getAppointmentsByPatient(patientCpf));
    }

    /**
     * Updates an appointment by ID.
     *
     * @param appointmentId the ID of the appointment to update
     * @param appointmentDTO the updated appointment data transfer object
     * @return a response entity with a success message
     */
    @PutMapping("/update/{appointmentId}")
    public ResponseEntity<String> updateAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentDTO appointmentDTO) {
        appointmentService.updateAppointment(appointmentId, appointmentDTO);
        return ResponseEntity.ok().body("Appointment updated successfully");
    }
}