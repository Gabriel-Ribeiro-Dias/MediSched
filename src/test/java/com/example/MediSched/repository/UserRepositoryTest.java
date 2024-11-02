package com.example.MediSched.repository;

import com.example.MediSched.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setUsername("john_doe");
        user1.setPassword("password123");

        User user2 = new User();
        user2.setUsername("jane_doe");
        user2.setPassword("password456");

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void findByUsername_returnsUserDetailsWhenExists() {
        Optional<UserDetails> userDetails = userRepository.findByUsername("john_doe");
        assertTrue(userDetails.isPresent());
        assertEquals("john_doe", userDetails.get().getUsername());
    }

    @Test
    void findByUsername_returnsEmptyWhenNotExists() {
        Optional<UserDetails> userDetails = userRepository.findByUsername("nonexistent_user");
        assertFalse(userDetails.isPresent());
    }
}