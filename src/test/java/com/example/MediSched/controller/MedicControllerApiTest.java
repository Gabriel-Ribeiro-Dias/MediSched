package com.example.MediSched.controller;

import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.service.MedicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class MedicControllerApiTest {

    @Mock
    private MedicService medicService;

    @InjectMocks
    private MedicController medicController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void updateMedicSuccessfully() {
        String crm = "12345";
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("Dr. John Doe");
        medicDTO.setCrm("12345");
        medicDTO.setExpertise("Cardiology");

        doNothing().when(medicService).updateMedic(crm, medicDTO);

        ResponseEntity<String> response = medicController.updateMedic(crm, medicDTO);

        assertEquals("Medic updated successfully", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(medicService, times(1)).updateMedic(crm, medicDTO);
    }

    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void getMedicSuccessfully() {
        String crm = "12345";
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("Dr. John Doe");
        medicDTO.setCrm(crm);
        medicDTO.setExpertise("Cardiology");

        when(medicService.getMedicByCrm(crm)).thenReturn(medicDTO);

        ResponseEntity<MedicDTO> response = medicController.getMedic(crm);

        assertEquals(medicDTO, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(medicService, times(1)).getMedicByCrm(crm);
    }

    @Test
@WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
void listMedicByCrmSuccessfully() {
    String crm = "12345";
    MedicDTO medicDTO = new MedicDTO();
    medicDTO.setName("Dr. John Doe");
    medicDTO.setCrm(crm);
    medicDTO.setExpertise("Cardiology");
    List<MedicDTO> medicDTOList = Collections.singletonList(medicDTO);

    when(medicService.listMedicsByCrm(crm)).thenReturn(medicDTOList);

    ResponseEntity<List<MedicDTO>> response = medicController.listMedicByCrm(crm);

    assertEquals(medicDTOList, response.getBody());
    assertEquals(200, response.getStatusCodeValue());
    verify(medicService, times(1)).listMedicsByCrm(crm);
}
}