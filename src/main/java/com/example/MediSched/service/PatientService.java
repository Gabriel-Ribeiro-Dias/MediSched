package com.example.MediSched.service;

import com.example.MediSched.exceptions.PatientAlreadyExistsException;
import com.example.MediSched.exceptions.PatientNotFoundException;
import com.example.MediSched.model.Patient;
import com.example.MediSched.model.User;
import com.example.MediSched.model.dto.PatientDTO;
import com.example.MediSched.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient create(@Valid PatientDTO data, User user) {
        if(patientRepository.findByCpf(data.getCpf()).isPresent()) throw new PatientAlreadyExistsException("Alredy exists a patient with this CPF.");
        Patient patient = convertToEntity(data);
        patient.setUser(user);
        return patientRepository.save(patient);
    }

    private Patient convertToEntity(PatientDTO data) {
        Patient patient = new Patient();
        patient.setName(data.getName());
        patient.setEmail(data.getEmail());
        patient.setCpf(data.getCpf());
        return patient;
    }
    public void deletePatient(String cpf){
        if(cpf == null || cpf.isEmpty()) throw new IllegalArgumentException("CPF cannot be null or empty");
        Patient patient = patientRepository.findByCpf(cpf).orElseThrow(() -> new PatientNotFoundException());
        patientRepository.deleteById(patient.getId());
    }
    public void updatePatient(String cpf, @Valid PatientDTO patient){
        if(cpf == null || cpf.isEmpty()) throw new IllegalArgumentException("CPF cannot be null or empty");
        Patient patientToUpdate = patientRepository.findByCpf(cpf).orElseThrow(() -> new PatientNotFoundException());
        patientToUpdate.setName(patient.getName());
        patientToUpdate.setEmail(patient.getEmail());
        patientToUpdate.setCpf(patient.getCpf());
        patientRepository.save(patientToUpdate);
    }
    public PatientDTO getPatientByCpf(String cpf){
        if(cpf == null || cpf.isEmpty()) throw new IllegalArgumentException("CPF cannot be null or empty");
        PatientDTO patient = convertToDTO(patientRepository.findByCpf(cpf).orElseThrow(() -> new PatientNotFoundException()));
        if(patient == null) throw new PatientNotFoundException();
        return patient;
    }
    public List<PatientDTO> getAllPatients(){
        List<PatientDTO> patientDTOS = new ArrayList<>();
        for(Patient patient : patientRepository.findAll()){
            patientDTOS.add(convertToDTO(patient));
        }
        return patientDTOS;
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName(patient.getName());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setCpf(patient.getCpf());
        return patientDTO;
    }

    public List<PatientDTO> listPatientsByCpf(String cpf) {
        if(cpf == null || cpf.isEmpty()) throw new IllegalArgumentException("CPF cannot be null or empty");
        List<PatientDTO> patientDTOS = new ArrayList<>();
        for(Patient patient : patientRepository.findAllByCpf(cpf)){
            patientDTOS.add(convertToDTO(patient));
        }
        return patientDTOS;
    }
}
