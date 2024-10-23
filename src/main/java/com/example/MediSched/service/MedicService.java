package com.example.MediSched.service;

import com.example.MediSched.exceptions.MedicAlreadyExistsException;
import com.example.MediSched.exceptions.MedicNotFoundException;
import com.example.MediSched.model.Medic;
import com.example.MediSched.model.User;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.repository.MedicRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class MedicService {
    @Autowired
    private MedicRepository medicRepository;

    public Medic create(@Valid MedicDTO data, User user) {
        if(!medicRepository.findByCrm(data.getCrm()).isEmpty()) throw new MedicAlreadyExistsException();
        Medic medic = convertToEntity(data);
        medic.setUser(user);
        return medicRepository.save(medic);
    }

    private Medic convertToEntity(MedicDTO data) {
        Medic medic = new Medic();
        medic.setName(data.getName());
        medic.setCrm(data.getCrm());
        medic.setExpertise(data.getExpertise());
        return medic;
    }

    public List<MedicDTO> getAllMedics() {
        List<MedicDTO> medicDTOS = new ArrayList<>();
        for (Medic medic : medicRepository.findAll()) {
            medicDTOS.add(ConvertToDTO(medic));
        }
        return medicDTOS;
    }
    public MedicDTO getMedicByCrm(String crm) {
        if (crm == null || crm.isEmpty()) throw new IllegalArgumentException("CRM cannot be null or empty");
        MedicDTO medic = ConvertToDTO(medicRepository.findByCrm(crm).orElseThrow(() -> new MedicNotFoundException()));
        if (medic == null) throw new MedicNotFoundException();
        return medic;
    }

    private MedicDTO ConvertToDTO(Medic medic) {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName(medic.getName());
        medicDTO.setCrm(medic.getCrm());
        medicDTO.setExpertise(medic.getExpertise());
        return medicDTO;
    }

    public void updateMedic(String crm, @Valid MedicDTO medic) {
        Optional<Medic> medicOptional = medicRepository.findByCrm(crm);
        if (medicOptional.isEmpty()) throw new MedicNotFoundException();
        Medic medicToUpdate = medicOptional.get();
        medicToUpdate.setName(medic.getName());
        medicToUpdate.setExpertise(medic.getExpertise());
        medicToUpdate.setCrm(medic.getCrm());
        medicRepository.save(medicToUpdate);

    }
    public void deleteMedic(String crm) {
        if (crm.isBlank() || crm == null) throw new IllegalArgumentException("CRM cannot be null or empty");
        Medic medic = medicRepository.findByCrm(crm).orElseThrow(()->new MedicNotFoundException());
        medicRepository.deleteById(medic.getId());
    }

    public List<MedicDTO> listMedicsByCrm(String crm) {
        if (crm == null || crm.isEmpty()) throw new IllegalArgumentException("CRM cannot be null or empty");
        List<MedicDTO> medicDTOS = new ArrayList<>();
        for (Medic medic : medicRepository.findAllByCrm(crm)) {
            medicDTOS.add(ConvertToDTO(medic));
        }
        return medicDTOS;
    }
}
