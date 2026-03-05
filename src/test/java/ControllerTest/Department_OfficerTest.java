package ControllerTest;

import dolpi.CivicInsight.CivicInsightApplication;
import dolpi.CivicInsight.DTO.OfficerLogin;
import dolpi.CivicInsight.DTO.officerDTO;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.JwtToken.jwttoken;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Service.MyUserDetails;
import dolpi.CivicInsight.Service.OfficerService;
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
@AutoConfigureMockMvc(addFilters = false)
public class Department_OfficerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OfficerRepo officerRepo;

    @MockitoBean
    private OfficerService officerService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private MyUserDetails myUserDetails;

    @MockitoBean
    private jwttoken jwttoken;

    // 1. Test for Successful Signup
    @Test
    void testSignupOfficer_Success() throws Exception {
        officerDTO dto = new officerDTO();
        dto.setUsername("officer_rahul");
        dto.setEmail("rahul@civic.com");
        dto.setPassword("pass123");
        dto.setDepartment("MCA");
        dto.setCity("M");
        dto.setName("Manas");

        // Mock checks: User doesn't exist
        Mockito.when(officerRepo.findByUsername("officer_rahul")).thenReturn(null);
        Mockito.when(officerRepo.existsByEmail("rahul@civic.com")).thenReturn(false);
        Mockito.when(officerService.Createofficer(any(officerDTO.class))).thenReturn(String.valueOf(new OfficerEnty()));

        mockMvc.perform(post("/create/department_user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    // 2. Test for Invalid Email Format
    @Test
    void testSignupOfficer_InvalidEmail() throws Exception {
        officerDTO dto = new officerDTO();
        dto.setUsername("officer_rahul");
        dto.setEmail("civic.com");
        dto.setPassword("pass123");
        dto.setDepartment("MCA");
        dto.setCity("M");
        dto.setName("Manas");

        mockMvc.perform(post("/create/department_user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // 3. Test for Successful Login
    @Test
    void testLoginOfficer_Success() throws Exception {
        OfficerLogin loginReq = new OfficerLogin();
        loginReq.setUsername("officer_rahul");
        loginReq.setPassword("pass123");

        OfficerEnty officer = new OfficerEnty();
        officer.setUsername("officer_rahul");
        officer.setRoles(Collections.singletonList("DEPT_HEAD"));

        UserDetails userDetails = Mockito.mock(UserDetails.class);

        // Mocking authentication flow
        Mockito.when(officerRepo.findByUsername("officer_rahul")).thenReturn(officer);
        Mockito.when(myUserDetails.loadUserByUsername("officer_rahul")).thenReturn(userDetails);
        Mockito.when(jwttoken.generateToken("officer_rahul")).thenReturn("dummy-jwt-token");

        mockMvc.perform(post("/create/department_user/login/officer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}