package com.example.MediSched.controller;

import com.example.MediSched.infra.security.TokenService;
import com.example.MediSched.model.*;
import com.example.MediSched.model.dto.*;
import com.example.MediSched.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related requests.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    /**
     * Constructs an AuthenticationController with the specified dependencies.
     *
     * @param authenticationManager the authentication manager
     * @param userService the user service
     * @param tokenService the token service
     */
    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserService userService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    /**
     * Authenticates a user and generates a token.
     *
     * @param data the authentication data transfer object containing username and password
     * @return a ResponseEntity containing the generated token
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    /**
     * Registers a new user.
     *
     * @param userDTO the user data transfer object containing user details
     * @return a ResponseEntity indicating successful registration
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        userService.create(userDTO);
        return ResponseEntity.ok().body("User registered successfully");
    }
}