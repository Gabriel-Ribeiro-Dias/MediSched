package com.example.MediSched.infra;

import com.example.MediSched.model.User;
import com.example.MediSched.model.enums.UserRole;
import com.example.MediSched.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostConstruct
    public void init() {
        // Verifica se o usuário admin já existe

        if (userRepository.findByUsername("admin") == null) {
            // Se não existe, cria o usuário admin
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("senha_admin"));
            adminUser.setRole(UserRole.ADMIN);

            // Salva o usuário admin
            userRepository.save(adminUser);
        }
    }
}
