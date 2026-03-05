package Service;

import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.OllamaResponse;
import dolpi.CivicInsight.Entity.ComplaintAnalysis;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Service.CompliantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompliantServiceTest {

    @Mock
    private ReportRepo reportRepo;

    @Mock
    private OfficerRepo officerRepo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CompliantService compliantService;

    @BeforeEach
    void setUp() {
        // Kyunki RestTemplate private final hai, hum use 'forcefully' inject kar rahe hain
        ReflectionTestUtils.setField(compliantService, "restTemplate", restTemplate);
    }

    @Test
    void testSendComplaint_Success() {
        // 1. Mock Input Complaint
        Complaints complaint = new Complaints();
        complaint.setId("COMP1");
        complaint.setCity("Indore");
        complaint.setComplaint("Water issue in my area");

        // 2. Mock Ollama API Response
        OllamaResponse mockOllama = new OllamaResponse();
        ComplaintAnalysis analysis = new ComplaintAnalysis();
        analysis.setDepartment("Water");
        analysis.setUrgency("HIGH");
        mockOllama.setAnalysis(analysis);

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(OllamaResponse.class)))
                .thenReturn(mockOllama);

        // 3. Mock Officer Data
        OfficerEnty officer = new OfficerEnty();
        officer.setId("OFF1");
        officer.setName("Rahul");
        officer.setDepartment("Water");
        officer.setCountReport(2);
        officer.setReportIds(new ArrayList<>());

        List<OfficerEnty> officers = new ArrayList<>();
        officers.add(officer);

        when(officerRepo.findByCityAndDepartment("Indore", "Water")).thenReturn(officers);

        // 4. Call Service
        String result = compliantService.sendcomplaint(complaint);

        // 5. Verifications
        assertTrue(result.contains("Rahul"));
        assertTrue(result.contains("Water"));

        // Check if data was saved
        verify(officerRepo, times(1)).save(any(OfficerEnty.class));
        verify(reportRepo, times(1)).save(any(Complaints.class));

        // Check if counter increased
        assert(officer.getCountReport() == 3);
    }
}