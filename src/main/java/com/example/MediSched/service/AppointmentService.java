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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class AppointmentService {
    private static final String MEDIC_NOT_FOUND = "Medic not found";
    private static final String PATIENT_NOT_FOUND = "Patient not found";
    private static final String MEDIC_NOT_AVAILABLE = "Medic not available at this time";
    private static final String APPOINTMENT_ALREADY_SCHEDULED = "Appointment already scheduled";
    private static final String APPOINTMENT_NOT_FOUND = "Appointment not found";

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private MedicRepository medicRepository;
    @Autowired
    private PatientRepository patientRepository;

    public void scheduleAppointment(@Valid AppointmentDTO appointmentDTO) {
        medicRepository.findByCrm(appointmentDTO.getMedic().getCrm()).orElseThrow(() -> new MedicNotFoundException(MEDIC_NOT_FOUND));
        patientRepository.findByCpf(appointmentDTO.getPatient().getCpf()).orElseThrow(() -> new PatientNotFoundException(PATIENT_NOT_FOUND));
        if (appointmentRepository.existsByMedicCrmAndDateAndTime(
                appointmentDTO.getMedic().getCrm(),
                appointmentDTO.getDate(),
                appointmentDTO.getTime())) { throw new MedicNotAvailableException(MEDIC_NOT_AVAILABLE); }
        if (appointmentRepository.existsByMedicCrmAndPatientCpfAndDateAndTime(
                appointmentDTO.getMedic().getCrm(),
                appointmentDTO.getPatient().getCpf(),
                appointmentDTO.getDate(),
                appointmentDTO.getTime())) { throw new AppointmentAlreadyScheduledException(APPOINTMENT_ALREADY_SCHEDULED); }
        Appointment appointment = convertToEntity(appointmentDTO);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setDate(appointment.getDate());
        appointmentDTO.setTime(appointment.getTime());

        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName(appointment.getMedic().getName());
        medicDTO.setCrm(appointment.getMedic().getCrm());
        medicDTO.setExpertise(appointment.getMedic().getExpertise());
        appointmentDTO.setMedic(medicDTO);

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName(appointment.getPatient().getName());
        patientDTO.setEmail(appointment.getPatient().getEmail());
        patientDTO.setCpf(appointment.getPatient().getCpf());
        appointmentDTO.setPatient(patientDTO);
        appointmentDTO.setStatus(appointment.getStatus());
        return appointmentDTO;
    }

    private Appointment convertToEntity(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentDTO.getDate());
        appointment.setTime(appointmentDTO.getTime());
        appointment.setMedic(medicRepository.findByCrm(appointmentDTO.getMedic().getCrm()).get());
        appointment.setPatient(patientRepository.findByCpf(appointmentDTO.getPatient().getCpf()).get());
        return appointment;
    }

    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException(APPOINTMENT_NOT_FOUND));
        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);
    };
    public List<AppointmentDTO> getAppointments() {
        List<AppointmentDTO> appointmentDTOList = new ArrayList<>();
        List<Appointment> appointmentList = appointmentRepository.findAll();
        for (Appointment appointment : appointmentList) {
            appointmentDTOList.add(convertToDTO(appointment));
        }
        return appointmentDTOList;
    }
    public AppointmentDTO getAppointment(Long appointmentId) {
        return convertToDTO(appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException(APPOINTMENT_NOT_FOUND)));
    }
    public List<AppointmentDTO> getAppointmentsByMedic(String medicCrm) {
        medicRepository.findByCrm(medicCrm).orElseThrow(() -> new MedicNotFoundException(MEDIC_NOT_FOUND));
        List<AppointmentDTO> appointmentDTOList = new ArrayList<>();
        List<Appointment> appointmentList = appointmentRepository.findByMedicCrm(medicCrm);
        for (Appointment appointment : appointmentList) {
            appointmentDTOList.add(convertToDTO(appointment));
        }
        return appointmentDTOList;
    }
    public List<AppointmentDTO> getAppointmentsByPatient(String patientCpf) {
        patientRepository.findByCpf(patientCpf).orElseThrow(() -> new PatientNotFoundException(PATIENT_NOT_FOUND));
        List<AppointmentDTO> appointmentDTOList = new ArrayList<>();
        List<Appointment> appointmentList = appointmentRepository.findByPatientCpf(patientCpf);
        for (Appointment appointment : appointmentList) {
            appointmentDTOList.add(convertToDTO(appointment));
        }
        return appointmentDTOList;
    }
    public Appointment updateAppointment(Long appointmentId, @Valid AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException(APPOINTMENT_NOT_FOUND));
        if (appointmentRepository.existsByMedicCrmAndDateAndTime(appointment.getMedic().getCrm(), appointmentDTO.getDate(), appointmentDTO.getTime())) {
            throw new MedicNotAvailableException(MEDIC_NOT_AVAILABLE);
        }
        appointment.setDate(appointmentDTO.getDate());
        appointment.setTime(appointmentDTO.getTime());
        appointment.setMedic(medicRepository.findByCrm(appointmentDTO.getMedic().getCrm()).get());
        appointment.setPatient(patientRepository.findByCpf(appointmentDTO.getPatient().getCpf()).get());
        appointment.setStatus(appointmentDTO.getStatus());
        return appointmentRepository.save(appointment);
    }
    public Appointment updateAppointmentStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException(APPOINTMENT_NOT_FOUND));
        AppointmentStatus newAppointmentStatus;
        try {
            newAppointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidAppointmentStatusException("Invalid appointment status: " + status);
        }
        appointment.setStatus(newAppointmentStatus);
        return appointmentRepository.save(appointment);
    }
}