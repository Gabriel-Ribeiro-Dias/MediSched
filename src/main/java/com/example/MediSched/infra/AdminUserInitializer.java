package com.example.MediSched.infra;

import com.example.MediSched.model.User;
import com.example.MediSched.model.dto.UserDTO;
import com.example.MediSched.model.enums.UserRole;
import com.example.MediSched.repository.UserRepository;
import com.example.MediSched.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostConstruct
    public void init() {
        if(!userService.getAllUsers().stream().anyMatch(userDTO -> userDTO.getUsername().equals("admin"))) {
            UserDTO adminUser = new UserDTO();
            adminUser.setUsername("admin");
            adminUser.setPassword("senha_admin");
            userService.create(adminUser);
        }
    }
}
