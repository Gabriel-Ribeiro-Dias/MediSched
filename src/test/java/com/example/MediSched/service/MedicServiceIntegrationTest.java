package com.example.MediSched.service;

import com.example.MediSched.exceptions.MedicAlreadyExistsException;
import com.example.MediSched.exceptions.MedicNotFoundException;
import com.example.MediSched.model.Medic;
import com.example.MediSched.model.User;
import com.example.MediSched.model.dto.MedicDTO;
import com.example.MediSched.repository.MedicRepository;
import com.example.MediSched.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class MedicServiceIntegrationTest {

    @Autowired
    private MedicService medicService;

    @Autowired
    private MedicRepository medicRepository;
    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Configura um usuário de teste
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        userRepository.save(testUser);
    }


    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Existing Medic', '12345', 'Cardiology')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_medic WHERE crm = '54321'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createMedicSuccessfully() {
        // Criação do DTO
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("John Doe");
        medicDTO.setCrm("54321"); // Crm diferente para evitar conflito
        medicDTO.setExpertise("Cardiology");

        // Chama o método a ser testado
        Medic result = medicService.create(medicDTO, testUser);

        // Verifica se o resultado não é nulo
        assertNotNull(result);
        assertEquals(medicDTO.getName(), result.getName());
        assertEquals(medicDTO.getCrm(), result.getCrm());

        // Verifica se o medic foi salvo no banco de dados
        Optional<Medic> savedMedic = medicRepository.findByCrm(medicDTO.getCrm());
        assertTrue(savedMedic.isPresent());
        assertEquals(medicDTO.getName(), savedMedic.get().getName());
    }


    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Existing Medic', '12346', 'Cardiology')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_medic WHERE crm = '12346'",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createMedicThrowsExceptionWhenCrmExists() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setName("John Doe");
        medicDTO.setCrm("12346"); // Crm que já existe
        medicDTO.setExpertise("Cardiology");

        assertThrows(MedicAlreadyExistsException.class, () -> medicService.create(medicDTO, testUser));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. House', '12345', 'Cardiology')",
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Strange', '67890', 'Surgery')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM tb_medic WHERE crm IN ('12345', '67890')", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllMedicsSuccessfully() {
        // Obtém todos os médicos
        List<MedicDTO> medics = medicService.getAllMedics();

        assertEquals(2, medics.size());
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Who', '54321', 'Neurology')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_medic WHERE crm = '54321'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getMedicByCrmSuccessfully() {
        // Obtém o médico pelo CRM
        MedicDTO result = medicService.getMedicByCrm("54321");

        assertNotNull(result);
        assertEquals("Dr. Who", result.getName());
        assertEquals("54321", result.getCrm());
        assertEquals("Neurology", result.getExpertise());
    }

    @Test
    void getMedicByCrmThrowsExceptionWhenCrmIsNull() {
        assertThrows(IllegalArgumentException.class, () -> medicService.getMedicByCrm(null));
    }

    @Test
    @Sql(statements = "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Jekyll', '11111', 'Psychiatry')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM tb_medic WHERE crm = '11111'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateMedicSuccessfully() {
        MedicDTO updatedMedicDTO = new MedicDTO();
        updatedMedicDTO.setName("Dr. Jekyll");
        updatedMedicDTO.setCrm("11111");
        updatedMedicDTO.setExpertise("Psychiatry - Updated");

        medicService.updateMedic("11111", updatedMedicDTO);

        // Verifica se o médico foi atualizado
        MedicDTO result = medicService.getMedicByCrm("11111");
        assertEquals("Psychiatry - Updated", result.getExpertise());
    }

    @Test
    void updateMedicThrowsExceptionWhenMedicNotFound() {
        MedicDTO updatedMedicDTO = new MedicDTO();
        updatedMedicDTO.setName("Dr. Unknown");
        updatedMedicDTO.setCrm("22222");
        updatedMedicDTO.setExpertise("Unknown");

        assertThrows(MedicNotFoundException.class, () -> medicService.updateMedic("22222", updatedMedicDTO));
    }

    @Test
    @Sql(statements = "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. Evil', '33333', 'Villainy')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM tb_medic WHERE crm = '33333'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteMedicSuccessfully() {
        medicService.deleteMedic("33333");

        // Verifica se o médico foi deletado
        assertThrows(MedicNotFoundException.class, () -> medicService.getMedicByCrm("33333"));
    }

    @Test
    void deleteMedicThrowsExceptionWhenCrmIsNull() {
        assertThrows(IllegalArgumentException.class, () -> medicService.deleteMedic(null));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. A', '44444', 'Cardiology')",
            "INSERT INTO tb_medic (name, crm, expertise) VALUES ('Dr. B', '44444', 'Neurology')"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {
            "DELETE FROM tb_medic WHERE crm = '44444'"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void listMedicsByCrmSuccessfully() {
        // Obtém médicos pelo CRM
        List<MedicDTO> medics = medicService.listMedicsByCrm("44444");

        assertEquals(2, medics.size());
        assertEquals("Dr. A", medics.get(0).getName());
        assertEquals("Dr. B", medics.get(1).getName());
    }

    @Test
    void listMedicsByCrmThrowsExceptionWhenCrmIsNull() {
        assertThrows(IllegalArgumentException.class, () -> medicService.listMedicsByCrm(null));
    }
}