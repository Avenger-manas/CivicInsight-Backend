package ControllerTest;

import dolpi.CivicInsight.CivicInsightApplication;
import dolpi.CivicInsight.Service.OfficerAninalyisisComplaintsService;
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
@AutoConfigureMockMvc(addFilters = false) // Security bypass karne ke liye
public class OfficerAnalysisComplaintTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OfficerAninalyisisComplaintsService officerAnalysisService;

    // 1. Test for /fetch/complaint
    @Test
    void testAnalysisComplaint_Fetch() throws Exception {
        String testId = "OFF123";

        // Mocking service response
        Mockito.when(officerAnalysisService.FetchComplaints(testId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/department_officer/Analysis/fetch/complaint")
                        .param("Id", testId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 2. Test for /analysis/complaint (Two RequestParams)
    @Test
    void testReport_Analysis() throws Exception {
        String testId = "COMP789";
        String replyMessage = "This issue is under review.";

        Mockito.when(officerAnalysisService.AnalysisComplaint(testId, replyMessage))
                .thenReturn("Complaint Analyzed Successfully");

        mockMvc.perform(post("/department_officer/Analysis/analysis/complaint")
                        .param("Id", testId)
                        .param("message", replyMessage))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 3. Test for /find/Officer
    @Test
    void testFindOfficer() throws Exception {
        String testId = "OFF001";

        Mockito.when(officerAnalysisService.findOfficer(testId))
                .thenReturn(null);

        mockMvc.perform(post("/department_officer/Analysis/find/Officer")
                        .param("Id", testId))
                .andDo(print())
                .andExpect(status().isOk());
    }
}