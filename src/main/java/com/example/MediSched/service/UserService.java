package com.example.MediSched.service;

import com.example.MediSched.exceptions.UserAlredyExistsException;
import com.example.MediSched.model.Medic;
import com.example.MediSched.model.Patient;
import com.example.MediSched.model.User;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.model.dto.PatientDTO;
import com.example.MediSched.model.dto.UserDTO;
import com.example.MediSched.model.enums.UserRole;
import com.example.MediSched.repository.PatientRepository;
import com.example.MediSched.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final PatientService patientService;
    private final MedicService medicService;
    @Autowired
    public UserService(UserRepository userRepository, PatientService patientService, MedicService medicService) {
        this.userRepository = userRepository;
        this.patientService = patientService;
        this.medicService = medicService;
    }

    public void create(@Valid UserDTO userDTO) {
        if (!userRepository.findByUsername(userDTO.getUsername()).isEmpty()) {
            throw new UserAlredyExistsException("Already exists a user with this username.");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());
        User user = convertToEntity(userDTO);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        if(userDTO.getPatient() != null){
            Patient savedPatient = patientService.create(userDTO.getPatient(), user);
            user.setPatient(savedPatient);
            user.setRole(UserRole.PATIENT);
        }
        if(userDTO.getMedic() != null){
            Medic savedMedic = medicService.create(userDTO.getMedic(), user);
            user.setMedic(savedMedic);
            user.setRole(UserRole.MEDIC);
        }else user.setRole(UserRole.ADMIN);
        userRepository.save(user);
    }
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            userDTOS.add(convertToDTO(user));
        }
        return userDTOS;
    }
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        return user;
    }
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}
