package Service;

import dolpi.CivicInsight.DTO.officerDTO;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Service.OfficerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OfficerServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OfficerRepo officerRepo;

    @InjectMocks
    private OfficerService officerService;

    private officerDTO testDto;

    @BeforeEach
    void setUp() {
        testDto = new officerDTO();
        testDto.setName("Officer Rahul");
        testDto.setUsername("rahul_off");
        testDto.setPassword("plainPass123");
        testDto.setDepartment("Water");
        testDto.setEmail("rahul@civic.com");
        testDto.setCity("Bhopal");
    }

    @Test
    void testCreateOfficer_Success() {
        // 1. Mock Password Encoding
        String encodedPass = "hashed_password_xyz";
        Mockito.when(passwordEncoder.encode("plainPass123")).thenReturn(encodedPass);

        // 2. Call the service method
        String result = officerService.Createofficer(testDto);

        // 3. Assertions
        assertEquals("SuccesFully Create OFFICER", result);

        // 4. Verifications
        // Check if password was actually encoded before saving
        verify(passwordEncoder, times(1)).encode("plainPass123");

        // Check if repository.save() was called once with any OfficerEnty object
        verify(officerRepo, times(1)).save(any(OfficerEnty.class));
    }
}