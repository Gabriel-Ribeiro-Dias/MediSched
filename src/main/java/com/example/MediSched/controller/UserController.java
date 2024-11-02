package com.example.MediSched.controller;

import com.example.MediSched.model.dto.UserDTO;
import com.example.MediSched.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling user-related requests.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a UserController with the specified UserService.
     *
     * @param userService the user service
     */
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a ResponseEntity containing a list of user data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    /**
     * Deletes a user by username.
     *
     * @param username the username of the user to be deleted
     * @return a ResponseEntity containing a success message
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(String username){
        userService.deleteUser(username);
        return ResponseEntity.ok("User deleted successfully");
    }
}