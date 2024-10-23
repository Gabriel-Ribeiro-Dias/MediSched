//package com.example.MediSched.service;
//
//import com.example.MediSched.exceptions.UserAlredyExistsException;
//import com.example.MediSched.model.Medic;
//import com.example.MediSched.model.Patient;
//import com.example.MediSched.model.User;
//import com.example.MediSched.model.dto.MedicDTO;
//import com.example.MediSched.model.dto.PatientDTO;
//import com.example.MediSched.model.dto.UserDTO;
//import com.example.MediSched.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class UserServiceTest {
//
//    @Mock
//    UserRepository userRepository;
//    @Mock
//    PatientService patientService;
//    @Mock
//    MedicService medicService;
//    @InjectMocks
//    UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void create_withValidUserDTO_createsUserSuccessfully() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("testuser");
//        userDTO.setPassword("password");
//
//        when(userRepository.findByUsername("testuser")).thenReturn(null);
//
//        userService.create(userDTO);
//
//        verify(userRepository, times(2)).save(any(User.class));
//        verify(patientService, never()).create(any(PatientDTO.class), any(User.class));
//        verify(medicService, never()).create(any(MedicDTO.class), any(User.class));
//    }
//
//    @Test
//    void create_withExistingUsername_throwsUserAlredyExistsException() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("existinguser");
//        userDTO.setPassword("password");
//        userService.create(userDTO);
//        User existingUser = new User();
//        existingUser.setUsername("existinguser");
//
//        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));
//        UserAlredyExistsException exception = assertThrows(UserAlredyExistsException.class, () -> userService.create(userDTO));
//        assertEquals("Already exists a user with this username.", exception.getMessage());
//
//        verify(userRepository, never()).save(any(User.class));
//        verify(patientService, never()).create(any(PatientDTO.class), any(User.class));
//        verify(medicService, never()).create(any(MedicDTO.class), any(User.class));
//    }
//
//    @Test
//void create_withPatientDTO_createsPatientAndAssignsRole() {
//    UserDTO userDTO = new UserDTO();
//    userDTO.setUsername("testuser");
//    userDTO.setPassword("password");
//    PatientDTO patientDTO = new PatientDTO();
//    userDTO.setPatient(patientDTO);
//
//    when(userRepository.findByUsername("testuser")).thenReturn(null); // Mocking the repository to return null when findByUsername is called
//    when(patientService.create(any(PatientDTO.class), any(User.class))).thenReturn(new Patient()); // Mocking the patientService to return a new Patient when create is called
//
//    userService.create(userDTO);
//
//    verify(patientService).create(any(PatientDTO.class), any(User.class)); // Verifying that the patientService's create method was called
//    verify(userRepository, times(2)).save(any(User.class)); // Verifying that the userRepository's save method was called twice
//
//    // Additional verifications
//    User createdUser = (User) userRepository.findByUsername("testuser").get();
//    assertNotNull(createdUser); // Ensure the user was created
//    assertEquals("testuser", createdUser.getUsername()); // Ensure the username is correct
//    assertTrue(createdUser.getRole().equals("PATIENT")); // Ensure the user has the patient role
//}
//
//    @Test
//    void create_withMedicDTO_createsMedicAndAssignsRole() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("testuser");
//        userDTO.setPassword("password");
//        MedicDTO medicDTO = new MedicDTO();
//        userDTO.setMedic(medicDTO);
//
//        when(userRepository.findByUsername("testuser")).thenReturn(null);
//        when(medicService.create(any(MedicDTO.class), any(User.class))).thenReturn(new Medic());
//
//        userService.create(userDTO);
//
//        verify(medicService).create(any(MedicDTO.class), any(User.class));
//        verify(userRepository, times(2)).save(any(User.class));
//    }
//
//    @Test
//    void create_withoutPatientOrMedicDTO_assignsAdminRole() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("testuser");
//        userDTO.setPassword("password");
//
//        when(userRepository.findByUsername("testuser")).thenReturn(null);
//
//        userService.create(userDTO);
//
//        verify(userRepository, times(2)).save(any(User.class));
//    }
//}