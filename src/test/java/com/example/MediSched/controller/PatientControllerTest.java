package com.example.MediSched.controller;

import com.example.MediSched.model.dto.PatientDTO;
import com.example.MediSched.model.dto.UserDTO;
import com.example.MediSched.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void deletePatientReturnsHttp200() throws Exception {
        String cpf = "12345678900";
        doNothing().when(patientService).deletePatient(cpf);

        mockMvc.perform(MockMvcRequestBuilders.delete("/patient/delete")
                        .param("cpf", cpf))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient deleted successfully"));

        verify(patientService).deletePatient(cpf);
    }
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void getAllPatientsReturnsHttp200() throws Exception {
        List<PatientDTO> patients = List.of(new PatientDTO(), new PatientDTO());
        when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(MockMvcRequestBuilders.get("/patient/list"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(patientService).getAllPatients();
    }
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void updatePatientReturnsHttp200() throws Exception {
        String cpf = "12345678900";
        PatientDTO patient = new PatientDTO();
        patient.setCpf(cpf);
        patient.setName("Updated Name");
        patient.setEmail("updated.email@example.com");

        doNothing().when(patientService).updatePatient(cpf, patient);

        mockMvc.perform(MockMvcRequestBuilders.put("/patient/update")
                        .param("cpf", cpf)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\":\"12345678900\",\"name\":\"Updated Name\",\"email\":\"updated.email@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient updated successfully"));

        verify(patientService).updatePatient(cpf, patient);
    }
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void getPatientByCpfReturnsHttp200() throws Exception {
        String cpf = "12345678900";
        PatientDTO patient = new PatientDTO();
        patient.setCpf(cpf);
        patient.setName("Test Name");
        patient.setEmail("test.email@example.com");
        patient.setUser(new UserDTO());
        patient.setAppointmentList(List.of());

        when(patientService.getPatientByCpf(cpf)).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.get("/patient/")
                        .param("cpf", cpf)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"cpf\":\"12345678900\",\"name\":\"Test Name\",\"email\":\"test.email@example.com\",\"user\":{},\"appointmentList\":[]}"));

        verify(patientService).getPatientByCpf(cpf);
    }
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void listPatientByCpfReturnsHttp200() throws Exception {
        String cpf = "12345678900";
        PatientDTO patient1 = new PatientDTO();
        patient1.setCpf(cpf);
        patient1.setName("Patient One");
        patient1.setEmail("patient.one@example.com");

        PatientDTO patient2 = new PatientDTO();
        patient2.setCpf(cpf);
        patient2.setName("Patient Two");
        patient2.setEmail("patient.two@example.com");

        List<PatientDTO> patients = List.of(patient1, patient2);
        when(patientService.listPatientsByCpf(cpf)).thenReturn(patients);

        mockMvc.perform(MockMvcRequestBuilders.get("/patient/list-by-cpf")
                        .param("cpf", cpf)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"cpf\":\"12345678900\",\"name\":\"Patient One\",\"email\":\"patient.one@example.com\"},{\"cpf\":\"12345678900\",\"name\":\"Patient Two\",\"email\":\"patient.two@example.com\"}]"));

        verify(patientService).listPatientsByCpf(cpf);
    }



}