package ControllerTest; // <--- Sahi package

import dolpi.CivicInsight.CivicInsightApplication;
import dolpi.CivicInsight.Service.AdminFetchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CivicInsightApplication.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminFetchAllComplaintTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Spring Boot 3.5.11 ke liye sahi annotation
    private AdminFetchService adminFetchService;

    @Test
    void testFetchUnsolvedComplaint() throws Exception {
        String testId = "69a14d652d79e37cc8901305";

        // Mocking behavior
        Mockito.when(adminFetchService.FetchAllUnsolvedComplaints(testId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/admin/fetch/unsolved/complaint")
                        .param("Id", testId)) // RequestParam bhej rahe hain
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testFindAllOfficer() throws Exception {
        String testId = "69a14d652d79e37cc8901305";
        Mockito.when(adminFetchService.findAllOfficer(testId)).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/admin/fetch/find/AllOfficer")
                        .param("Id", testId))
                .andExpect(status().isOk());
    }

    @Test
    void testFetchAllComplaint() throws Exception {

        String adminId = "69a14d652d79e37cc8901305";


        Mockito.when(adminFetchService.fetchReport(adminId))
                .thenReturn(Collections.emptyList());

        // 2. Perform Request
        mockMvc.perform(post("/admin/fetch/reports")
                        .param("Id", adminId)) // RequestParam "Id" bhej rahe hain
                .andDo(print()) // Console mein result dekhne ke liye
                .andExpect(status().isOk()); // Check if status is 200 OK
    }
}