package ControllerTest;

import dolpi.CivicInsight.CivicInsightApplication;
import dolpi.CivicInsight.DTO.AdminDTO;
import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.JwtToken.jwttoken;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Service.AdminSignupService;
import dolpi.CivicInsight.Service.MyUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CivicInsightApplication.class)
@AutoConfigureMockMvc(addFilters = false) // S
public class AdminSignupTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminRepo adminRepo;

    @MockitoBean
    private AdminSignupService adminSignupService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private MyUserDetails myUserDetails;

    @MockitoBean
    private jwttoken jwttoken;

    // 1. Test for createAdmin (Signup)
    @Test
    void testCreateAdmin_Success() throws Exception {
        Admin admin = new Admin();
        admin.setUsername("manas_admin");
        admin.setEmail("manas@example.com");
        admin.setPassword("password123");

        // Mocking behavior
        Mockito.when(adminRepo.findByUsername("manas_admin")).thenReturn(null);
        Mockito.when(adminRepo.existsByEmail("manas@example.com")).thenReturn(false);
        Mockito.when(adminSignupService.CreateUser(any(Admin.class))).thenReturn(String.valueOf(admin));

        mockMvc.perform(post("/admin/signup/crateAdmin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin))) // Admin object ko JSON banaya
                .andDo(print())
                .andExpect(status().isCreated());
    }

    // 2. Test for Invalid Email Exception
    @Test
    void testCreateAdmin_InvalidEmail() throws Exception {
        Admin admin = new Admin();
        admin.setEmail("invalid-email");

        mockMvc.perform(post("/admin/signup/crateAdmin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // 3. Test for loginAdmin
    @Test
    void testLoginAdmin_Success() throws Exception {
        AdminDTO loginDTO = new AdminDTO();
        loginDTO.setUsername("manas_admin");
        loginDTO.setPassword("password123");

        Admin admin = new Admin();
        admin.setUsername("manas_admin");
        admin.setRoles(Collections.singletonList("ADMIn"));
        admin.setCity("ksk");
        admin.setUsername("223");
        admin.setName("1233");
        admin.setEmail("manasrastogi@2gmail.com");


        UserDetails userDetails = Mockito.mock(UserDetails.class);

        // Mocking all steps of login flow
        Mockito.when(adminRepo.findByUsername("manas_admin")).thenReturn(admin);
        Mockito.when(myUserDetails.loadUserByUsername("manas_admin")).thenReturn(userDetails);
        Mockito.when(jwttoken.generateToken("manas_admin")).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/admin/signup/login/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}