package com.example.MediSched.controller;

import com.example.MediSched.exceptions.MedicNotFoundException;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.service.MedicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MedicControllerApiTest {

    @MockBean
    private MedicService medicService;

    @Autowired
    private MedicController medicController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void updateMedicSuccessfully() throws Exception {
        String crm = "12345";
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("John Doe");

        mockMvc.perform(MockMvcRequestBuilders.put("/medic/update")
                        .param("crm", crm)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(medicDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Medic updated successfully"));

        verify(medicService, times(1)).updateMedic(crm, medicDTO);
    }

    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void getMedicByCrmReturnsHttp200() throws Exception {
        String crm = "12345";
        MedicDTO medicDTO = new MedicDTO();
        when(medicService.getMedicByCrm(crm)).thenReturn(medicDTO);

        mockMvc.perform(get("/medic")
                        .param("crm", crm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(medicDTO)));

        verify(medicService).getMedicByCrm(crm);
    }

    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void getMedicByCrmReturnsHttp404WhenNotFound() throws Exception {
        String crm = "12345";
        when(medicService.getMedicByCrm(crm)).thenThrow(new MedicNotFoundException("Could not find medic"));

        mockMvc.perform(get("/medic")
                        .param("crm", crm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(medicService).getMedicByCrm(crm);
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

        assertEquals(response.getBody(), medicDTOList);
        assertEquals(response.getStatusCodeValue(), 200);
        verify(medicService, times(1)).listMedicsByCrm(crm);
    }
}