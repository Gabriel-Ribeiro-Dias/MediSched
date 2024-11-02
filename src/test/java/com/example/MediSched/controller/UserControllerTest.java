package com.example.MediSched.controller;

import com.example.MediSched.model.dto.UserDTO;
import com.example.MediSched.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsersSuccessfully() {
        List<UserDTO> users = Collections.emptyList();
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertEquals(users, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void deleteUserSuccessfully() {
        String username = "testuser";
        doNothing().when(userService).deleteUser(username);

        ResponseEntity<String> response = userController.deleteUser(username);

        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser(username);
    }

    @Test
    void deleteUserNotFound() {
        String username = "nonexistentuser";
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(username);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userController.deleteUser(username);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).deleteUser(username);
    }
}