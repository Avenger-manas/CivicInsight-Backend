package ControllerTest;

import dolpi.CivicInsight.CivicInsightApplication;
import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Service.CompliantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CivicInsightApplication.class)
@AutoConfigureMockMvc(addFilters = false) // Security bypass taaki token ki zarurat na pade
public class ComplaintTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Java Object ko JSON String banane ke liye

    @MockitoBean
    private CompliantService compliantService;

    @Test
    void testSubmitComplaint() throws Exception {
        // 1. Mock Data taiyar karein
        Complaints complaintRequest = new Complaints();
        complaintRequest.setComplaint("Water Leakage");
        complaintRequest.setOfficerName("Main pipe is broken near Street 5");
        complaintRequest.setStatus("qq");
        complaintRequest.setCity("slw");
        complaintRequest.setOfficerName("2we");
        complaintRequest.setWard_number("123445");
        complaintRequest.setDepartmentName("kaka");
        complaintRequest.setName("eeq");
        complaintRequest.setUrgency("less");
        // Baki zaruri fields set karein

        // 2. Service behavior mock karein
        Mockito.when(compliantService.sendcomplaint(any(Complaints.class)))
                .thenReturn(String.valueOf(complaintRequest));

        // 3. Request perform karein
        mockMvc.perform(post("/user/complaint/submit")
                        .contentType(MediaType.APPLICATION_JSON) // Batana zaruri hai ki hum JSON bhej rahe hain
                        .content(objectMapper.writeValueAsString(complaintRequest))) // Object to JSON
                .andDo(print()) // Console mein request/response dekhein
                .andExpect(status().isCreated()); // Check for 201 Created
    }
}