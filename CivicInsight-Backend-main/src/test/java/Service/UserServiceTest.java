package Service;

import dolpi.CivicInsight.DTO.userdto;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Repository.UserRepo;
import dolpi.CivicInsight.Service.UserService;
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

@ExtendWith(MockitoExtension.class) // Mockito framework ko enable karne ke liye
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private userdto testDto;

    @BeforeEach
    void setUp() {
        testDto = new userdto();
        testDto.setName("Manas");
        testDto.setUsername("manas_user");
        testDto.setPassword("plainPass123");
        testDto.setEmail("manas@example.com");
    }

    @Test
    void testCreateUser_Success() {
        // 1. Mock behavior setup
        String encodedPass = "hashed_123_abc";
        Mockito.when(passwordEncoder.encode("plainPass123")).thenReturn(encodedPass);

        // 2. Call the service method
        String result = userService.CreateUser(testDto);

        // 3. Assertions (Check return message)
        assertEquals("SuccesFully Create User", result);

        // Verify: Password encode hua ya nahi
        verify(passwordEncoder, times(1)).encode("plainPass123");

        // Verify: Database mein save call hua ya nahi
        verify(userRepo, times(1)).save(any(UserEnity.class));
    }
}