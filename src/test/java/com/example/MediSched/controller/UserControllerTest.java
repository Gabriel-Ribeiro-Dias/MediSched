package com.example.MediSched.controller;

import com.example.MediSched.model.dto.UserDTO;
import com.example.MediSched.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<UserDTO> sampleUsers;

    @BeforeEach
    void setUp() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("user1");
        userDTO.setPassword("password1");
        sampleUsers = List.of(userDTO);
    }

    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void getAllUsersReturnsHttp200() throws Exception {
        when(userService.getAllUsers()).thenReturn(sampleUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(sampleUsers.size())))
                .andExpect(jsonPath("$[0].id").value(sampleUsers.get(0).getId()))
                .andExpect(jsonPath("$[0].username").value(sampleUsers.get(0).getUsername()))
                .andExpect(jsonPath("$[0].password").value(sampleUsers.get(0).getPassword()));
    }

    @Test
    @WithMockUser(username = "admin", password = "senha_admin", roles = "ADMIN")
    void getAllUsersReturnsListOfUserDTO() throws Exception {
        when(userService.getAllUsers()).thenReturn(sampleUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(sampleUsers.size())))
                .andExpect(jsonPath("$[0].id").value(sampleUsers.get(0).getId()))
                .andExpect(jsonPath("$[0].username").value(sampleUsers.get(0).getUsername()))
                .andExpect(jsonPath("$[0].password").value(sampleUsers.get(0).getPassword()));
    }
}