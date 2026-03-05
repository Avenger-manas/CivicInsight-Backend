package ControllerTest;

import dolpi.CivicInsight.CivicInsightApplication;
import dolpi.CivicInsight.DTO.UserLogin;
import dolpi.CivicInsight.DTO.userdto;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.JwtToken.jwttoken;
import dolpi.CivicInsight.Repository.UserRepo;
import dolpi.CivicInsight.Service.MyUserDetails;
import dolpi.CivicInsight.Service.UserService;
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
public class UserSignupTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepo userRepo;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private MyUserDetails myUserDetails;

    @MockitoBean
    private jwttoken jwttoken;

    // 1. Test for Successful Signup
    @Test
    void testSignupUser_Success() throws Exception {
        userdto dto = new userdto();
        dto.setUsername("testuser");
        dto.setEmail("user@example.com");
        dto.setPassword("pass123");
        dto.setName("ABV");


        // Mocking: User doesn't exist already
        Mockito.when(userRepo.findByUsername("testuser")).thenReturn(null);
        Mockito.when(userRepo.existsByEmail("user@example.com")).thenReturn(false);
        Mockito.when(userService.CreateUser(any(userdto.class))).thenReturn(String.valueOf(new UserEnity()));

        mockMvc.perform(post("/create/signup/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    // 2. Test for Invalid Email Format
    @Test
    void testSignupUser_InvalidEmail() throws Exception {
        userdto dto = new userdto();
        dto.setEmail("bad-email");

        mockMvc.perform(post("/create/signup/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // 3. Test for Successful Login
    @Test
    void testLoginUser_Success() throws Exception {
        UserLogin loginReq = new UserLogin();
        loginReq.setUsername("testuser");
        loginReq.setPassword("pass123");

        UserEnity user = new UserEnity();
        user.setUsername("testuser");
        user.setRoles(Collections.singletonList("PUBLIC_USER"));

        UserDetails userDetails = Mockito.mock(UserDetails.class);

        // Mocking authentication flow
        Mockito.when(userRepo.findByUsername("testuser")).thenReturn(user);
        Mockito.when(myUserDetails.loadUserByUsername("testuser")).thenReturn(userDetails);
        Mockito.when(jwttoken.generateToken("testuser")).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/create/login/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}