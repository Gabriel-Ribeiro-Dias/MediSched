package com.example.MediSched.controller;

import com.example.MediSched.infra.security.TokenService;
import com.example.MediSched.model.User;
import com.example.MediSched.model.dto.AuthenticationDTO;
import com.example.MediSched.model.dto.LoginResponseDTO;
import com.example.MediSched.model.dto.UserDTO;
import com.example.MediSched.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginWithValidCredentials() {
        AuthenticationDTO authDTO = new AuthenticationDTO("user", "password");
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(new User());
        when(tokenService.generateToken(any(User.class))).thenReturn("token");

        ResponseEntity<LoginResponseDTO> response = authenticationController.login(authDTO);

        assertEquals("token", response.getBody().token());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(any(User.class));
    }

    @Test
    void loginWithInvalidCredentials() {
        AuthenticationDTO authDTO = new AuthenticationDTO("user", "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Invalid credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationController.login(authDTO);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(0)).generateToken(any(User.class));
    }

    @Test
    void registerNewUser() {
        UserDTO userDTO = new UserDTO();
        doNothing().when(userService).create(userDTO);

        ResponseEntity<String> response = authenticationController.register(userDTO);

        assertEquals("User registered successfully", response.getBody());
        verify(userService, times(1)).create(userDTO);
    }

    @Test
    void registerExistingUser() {
        UserDTO userDTO = new UserDTO();
        doThrow(new RuntimeException("User already exists")).when(userService).create(userDTO);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationController.register(userDTO);
        });

        assertEquals("User already exists", exception.getMessage());
        verify(userService, times(1)).create(userDTO);
    }
}