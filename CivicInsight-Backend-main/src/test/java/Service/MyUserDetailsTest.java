package Service;

import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import dolpi.CivicInsight.Service.MyUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private AdminRepo adminRepo;

    @Mock
    private OfficerRepo officerRepo;

    @InjectMocks
    private MyUserDetails myUserDetails;

    // 1. Test when User is found
    @Test
    void testLoadByUsername_UserFound() {
        UserEnity user = new UserEnity();
        user.setUsername("rahul_user");
        user.setPassword("pass123");
        user.setRoles(List.of("USER"));

        Mockito.when(userRepo.findByUsername("rahul_user")).thenReturn(user);

        UserDetails result = myUserDetails.loadUserByUsername("rahul_user");

        assertEquals("rahul_user", result.getUsername());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    // 2. Test when Admin is found (User not found)
    @Test
    void testLoadByUsername_AdminFound() {
        Admin admin = new Admin();
        admin.setUsername("admin_manas");
        admin.setPassword("adminPass");
        admin.setRoles(List.of("ADMIN"));

        Mockito.when(userRepo.findByUsername("admin_manas")).thenReturn(null);
        Mockito.when(adminRepo.findByUsername("admin_manas")).thenReturn(admin);

        UserDetails result = myUserDetails.loadUserByUsername("admin_manas");

        assertEquals("admin_manas", result.getUsername());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    // 3. Test when Officer is found
    @Test
    void testLoadByUsername_OfficerFound() {
        OfficerEnty officer = new OfficerEnty();
        officer.setUsername("officer_sharma");
        officer.setPassword("offPass");
        officer.setRoles(List.of("OFFICER"));

        Mockito.when(userRepo.findByUsername("officer_sharma")).thenReturn(null);
        Mockito.when(adminRepo.findByUsername("officer_sharma")).thenReturn(null);
        Mockito.when(officerRepo.findByUsername("officer_sharma")).thenReturn(officer);

        UserDetails result = myUserDetails.loadUserByUsername("officer_sharma");

        assertEquals("officer_sharma", result.getUsername());
        assertEquals("offPass", result.getPassword());
    }

    // 4. Test when Nothing is found (Exception Case)
    @Test
    void testLoadByUsername_NotFound_ThrowsException() {
        Mockito.when(userRepo.findByUsername("unknown")).thenReturn(null);
        Mockito.when(adminRepo.findByUsername("unknown")).thenReturn(null);
        Mockito.when(officerRepo.findByUsername("unknown")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetails.loadUserByUsername("unknown");
        });
    }
}