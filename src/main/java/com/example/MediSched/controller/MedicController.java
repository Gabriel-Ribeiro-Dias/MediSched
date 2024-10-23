package com.example.MediSched.controller;

import com.example.MediSched.model.Medic;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.service.MedicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling medic-related requests.
 */
@RestController
@RequestMapping("/medic")
public class MedicController {

    private final MedicService medicService;

    /**
     * Constructs a MedicController with the specified MedicService.
     *
     * @param medicService the medic service
     */
    @Autowired
    public MedicController(MedicService medicService){
        this.medicService = medicService;
    }

    /**
     * Deletes a medic by CRM.
     *
     * @param crm the CRM of the medic to be deleted
     * @return a ResponseEntity indicating successful deletion
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMedic(@RequestParam String crm){
        medicService.deleteMedic(crm);
        return ResponseEntity.ok("Medic deleted successfully");
    }

    /**
     * Updates a medic's details.
     *
     * @param crm the CRM of the medic to be updated
     * @param medic the medic data transfer object containing updated details
     * @return a ResponseEntity indicating successful update
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateMedic(@RequestParam String crm, @RequestBody MedicDTO medic){
        medicService.updateMedic(crm, medic);
        return ResponseEntity.ok("Medic updated successfully");
    }

    /**
     * Retrieves a medic by CRM.
     *
     * @param crm the CRM of the medic to be retrieved
     * @return a ResponseEntity containing the medic data transfer object
     */
    @GetMapping
    public ResponseEntity<MedicDTO> getMedic(@RequestParam String crm){
        return ResponseEntity.ok(medicService.getMedicByCrm(crm));
    }

    /**
     * Lists medics by CRM.
     *
     * @param crm the CRM to filter medics
     * @return a ResponseEntity containing a list of medic data transfer objects
     */
    @GetMapping("/list-by-crm")
    public ResponseEntity<List<MedicDTO>> listMedicByCrm(@RequestParam String crm){
        return ResponseEntity.ok(medicService.listMedicsByCrm(crm));
    }
}