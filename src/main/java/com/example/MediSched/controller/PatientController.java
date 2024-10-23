package com.example.MediSched.controller;

import com.example.MediSched.model.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.MediSched.service.PatientService;

import java.util.List;

/**
 * Controller for handling patient-related requests.
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    /**
     * Constructs a PatientController with the specified PatientService.
     *
     * @param patientService the patient service
     */
    @Autowired
    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    /**
     * Deletes a patient by CPF.
     *
     * @param cpf the CPF of the patient to be deleted
     * @return a ResponseEntity indicating successful deletion
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePatient(@RequestParam String cpf){
        patientService.deletePatient(cpf);
        return ResponseEntity.ok("Patient deleted successfully");
    }

    /**
     * Retrieves a list of all patients.
     *
     * @return a ResponseEntity containing a list of patient data transfer objects
     */
    @GetMapping("/list")
    public ResponseEntity<List<PatientDTO>> getAllPatients(){
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    /**
     * Updates a patient's details.
     *
     * @param cpf the CPF of the patient to be updated
     * @param patient the patient data transfer object containing updated details
     * @return a ResponseEntity indicating successful update
     */
    @PutMapping("/update")
    public ResponseEntity<String> updatePatient(@RequestParam String cpf, @RequestBody PatientDTO patient){
        patientService.updatePatient(cpf, patient);
        return ResponseEntity.ok("Patient updated successfully");
    }

    /**
     * Retrieves a patient by CPF.
     *
     * @param cpf the CPF of the patient to be retrieved
     * @return a ResponseEntity containing the patient data transfer object
     */
    @GetMapping("/")
    public ResponseEntity<PatientDTO> getPatientByCpf(@RequestParam String cpf){
        return ResponseEntity.ok(patientService.getPatientByCpf(cpf));
    }

    /**
     * Lists patients by CPF.
     *
     * @param cpf the CPF to filter patients
     * @return a ResponseEntity containing a list of patient data transfer objects
     */
    @GetMapping("/list-by-cpf")
    public ResponseEntity<List<PatientDTO>> listPatientByCpf(@RequestParam String cpf){
        return ResponseEntity.ok(patientService.listPatientsByCpf(cpf));
    }

}