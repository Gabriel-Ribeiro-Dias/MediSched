package com.example.MediSched.controller;

import com.example.MediSched.model.dto.AppointmentDTO;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.model.dto.PatientDTO;
import com.example.MediSched.model.enums.AppointmentStatus;
import com.example.MediSched.service.AppointmentService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerApiTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
@WithMockUser(username = "admin", password = "senha_admin", roles = "PATIENT")
void scheduleAppointmentReturnsHttp200() throws Exception {
    AppointmentDTO appointmentDTO = new AppointmentDTO();
    appointmentDTO.setDate("12/12/2021");
    appointmentDTO.setTime("12:00");
    appointmentDTO.setMedic(new MedicDTO());
    appointmentDTO.setPatient(new PatientDTO());
    appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

    doNothing().when(appointmentService).scheduleAppointment(any(AppointmentDTO.class));

    mockMvc.perform(post("/appointment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new JSONObject()
                            .put("date", appointmentDTO.getDate())
                            .put("time", appointmentDTO.getTime())
                            .put("medic", new JSONObject())
                            .put("patient", new JSONObject())
                            .put("status", appointmentDTO.getStatus().toString())
                            .toString()))
            .andExpect(status().isOk())
            .andExpect(content().string("Appointment scheduled successfully"));

    verify(appointmentService).scheduleAppointment(any(AppointmentDTO.class));
}
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void cancelAppointmentReturnsHttp200() throws Exception {
        Long appointmentId = 1L;

        doNothing().when(appointmentService).cancelAppointment(appointmentId);

        mockMvc.perform(delete("/appointment/cancel/{appointmentId}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment canceled successfully"));

        verify(appointmentService).cancelAppointment(appointmentId);
}
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void getAppointmentsReturnsHttp200() throws Exception {
        List<AppointmentDTO> appointments = List.of(new AppointmentDTO(), new AppointmentDTO());
        when(appointmentService.getAppointments()).thenReturn(appointments);

        mockMvc.perform(get("/appointment/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(appointmentService).getAppointments();
    }
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "PATIENT")
    void getAppointmentByIdReturnsHttp200() throws Exception {
        Long appointmentId = 1L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointmentId);
        appointmentDTO.setDate("12/12/2021");
        appointmentDTO.setTime("12:00");
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.setPatient(new PatientDTO());
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentService.getAppointment(appointmentId)).thenReturn(appointmentDTO);

        mockMvc.perform(get("/appointment/{appointmentId}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new JSONObject()
                        .put("id", appointmentDTO.getId())
                        .put("date", appointmentDTO.getDate())
                        .put("time", appointmentDTO.getTime())
                        .put("medic", new JSONObject())
                        .put("patient", new JSONObject())
                        .put("status", appointmentDTO.getStatus().toString())
                        .toString()));

        verify(appointmentService).getAppointment(appointmentId);
    }
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "PATIENT")
    void listAppointmentsByMedicReturnsHttp200() throws Exception {
        String medicCrm = "123456";
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setCrm(medicCrm);

        AppointmentDTO appointment1 = new AppointmentDTO();
        appointment1.setDate("12/12/2021");
        appointment1.setTime("12:00");
        appointment1.setMedic(medicDTO);
        appointment1.setPatient(new PatientDTO());
        appointment1.setStatus(AppointmentStatus.SCHEDULED);

        AppointmentDTO appointment2 = new AppointmentDTO();
        appointment2.setDate("13/12/2021");
        appointment2.setTime("14:00");
        appointment2.setMedic(medicDTO);
        appointment2.setPatient(new PatientDTO());
        appointment2.setStatus(AppointmentStatus.SCHEDULED);

        List<AppointmentDTO> appointments = List.of(appointment1, appointment2);
        when(appointmentService.getAppointmentsByMedic(medicCrm)).thenReturn(appointments);

        mockMvc.perform(get("/appointment/list-by-medic")
                        .param("medicCrm", medicCrm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"date\":\"12/12/2021\"," +
                                "\"time\":\"12:00\"," +
                                "\"medic\":{\"crm\":\"123456\"}," +
                                "\"patient\":{}," +
                                "\"status\":\"SCHEDULED\"}," +
                                "{\"date\":\"13/12/2021\"," +
                                "\"time\":\"14:00\"," +
                                "\"medic\":{\"crm\":\"123456\"}," +
                                "\"patient\":{}," +
                                "\"status\":\"SCHEDULED\"}]"));
        verify(appointmentService).getAppointmentsByMedic(medicCrm);
    }
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "PATIENT")
    void listAppointmentsByPatientReturnsHttp200() throws Exception {
        String patientCpf = "12345678900";
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setCpf(patientCpf);

        AppointmentDTO appointment1 = new AppointmentDTO();
        appointment1.setDate("12/12/2021");
        appointment1.setTime("12:00");
        appointment1.setMedic(new MedicDTO());
        appointment1.setPatient(patientDTO);
        appointment1.setStatus(AppointmentStatus.SCHEDULED);

        AppointmentDTO appointment2 = new AppointmentDTO();
        appointment2.setDate("13/12/2021");
        appointment2.setTime("14:00");
        appointment2.setMedic(new MedicDTO());
        appointment2.setPatient(patientDTO);
        appointment2.setStatus(AppointmentStatus.SCHEDULED);

        List<AppointmentDTO> appointments = List.of(appointment1, appointment2);
        when(appointmentService.getAppointmentsByPatient(patientCpf)).thenReturn(appointments);

        mockMvc.perform(get("/appointment/list-by-patient")
                        .param("patientCpf", patientCpf)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"date\":\"12/12/2021\"," +
                                "\"time\":\"12:00\"," +
                                "\"medic\":{}," +
                                "\"patient\":{\"cpf\":\"12345678900\"}," +
                                "\"status\":\"SCHEDULED\"}," +
                                "{\"date\":\"13/12/2021\"," +
                                "\"time\":\"14:00\"," +
                                "\"medic\":{}," +
                                "\"patient\":{\"cpf\":\"12345678900\"}," +
                                "\"status\":\"SCHEDULED\"}]"));

        verify(appointmentService).getAppointmentsByPatient(patientCpf);
    }
    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "PATIENT")
    void updateAppointmentReturnsHttp200() throws Exception {
        Long appointmentId = 1L;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDate("12/12/2021");
        appointmentDTO.setTime("12:00");
        appointmentDTO.setMedic(new MedicDTO());
        appointmentDTO.setPatient(new PatientDTO());
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);

        doAnswer(invocation -> null).when(appointmentService).updateAppointment(eq(appointmentId), any(AppointmentDTO.class));
        mockMvc.perform(put("/appointment/update/{appointmentId}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new JSONObject()
                                .put("date", appointmentDTO.getDate())
                                .put("time", appointmentDTO.getTime())
                                .put("medic", new JSONObject())
                                .put("patient", new JSONObject())
                                .put("status", appointmentDTO.getStatus().toString())
                                .toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment updated successfully"));

        verify(appointmentService).updateAppointment(eq(appointmentId), any(AppointmentDTO.class));
    }


}