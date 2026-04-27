package Service;

import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Service.AdminSignupService;
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
public class AdminSignupServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminRepo adminRepo;

    @InjectMocks
    private AdminSignupService adminSignupService;

    private Admin inputAdmin;

    @BeforeEach
    void setUp() {
        inputAdmin = new Admin();
        inputAdmin.setName("Manas");
        inputAdmin.setUsername("manas_admin");
        inputAdmin.setPassword("plainPassword123");
        inputAdmin.setEmail("manas@example.com");
        inputAdmin.setCity("Indore");
    }

    @Test
    void testCreateUser_Success() {
        // making password
        String encodedPass = "encoded_hash_789";
        Mockito.when(passwordEncoder.encode("plainPassword123")).thenReturn(encodedPass);

        // 2. Call the service method
        String result = adminSignupService.CreateUser(inputAdmin);

        // 3. Assertions
        assertEquals("SuccesFully Create User", result);

        // 4. Verification
        // Check if password was actually encoded
        verify(passwordEncoder, times(1)).encode("plainPassword123");

        // Check if repository.save() was called with an Admin object
        verify(adminRepo, times(1)).save(any(Admin.class));
    }
}