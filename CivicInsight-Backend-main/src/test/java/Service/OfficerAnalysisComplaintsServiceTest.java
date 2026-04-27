package Service;

import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Service.OfficerAninalyisisComplaintsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfficerAnalysisComplaintsServiceTest {

    @Mock
    private ReportRepo reportRepo;

    @Mock
    private OfficerRepo officerRepo;

    @InjectMocks
    private OfficerAninalyisisComplaintsService officerService;

    // 1. Test for FetchComplaints
    @Test
    void testFetchComplaints() {
        String officerId = "OFF123";
        List<Complaints> mockList = List.of(new Complaints(), new Complaints());

        when(reportRepo.findByOfficerId(officerId)).thenReturn(mockList);

        List<Complaints> result = officerService.FetchComplaints(officerId);

        assertEquals(2, result.size());
        verify(reportRepo, times(1)).findByOfficerId(officerId);
    }

    // 2. Test for AnalysisComplaint (Success Case)
    @Test
    void testAnalysisComplaint_Success() {
        String complaintId = "C1";
        String officerId = "OFF1";
        String reply = "Resolved the issue.";

        // Mock Complaint
        Complaints complaint = new Complaints();
        complaint.setId(complaintId);
        complaint.setOfficerId(officerId);
        complaint.setListReport(new ArrayList<>());
        complaint.setStatus("Pending");

        // Mock Officer
        OfficerEnty officer = new OfficerEnty();
        officer.setId(officerId);
        officer.setCountReport(5);

        when(reportRepo.findById(complaintId)).thenReturn(Optional.of(complaint));
        when(officerRepo.findById(officerId)).thenReturn(Optional.of(officer));

        String result = officerService.AnalysisComplaint(complaintId, reply);

        // Assertions
        assertEquals("Complaint updated successfully!", result);
        assertEquals("Analyzed", complaint.getStatus());
        assertEquals(4, officer.getCountReport());
        assertTrue(complaint.getListReport().contains(reply));

        // Verify saves
        verify(officerRepo).save(officer);
        verify(reportRepo).save(complaint);
    }

    // 3. Test for findOfficer
    @Test
    void testFindOfficer_Success() {
        String id = "OFF1";
        OfficerEnty officer = new OfficerEnty();
        officer.setId(id);

        when(officerRepo.findById(id)).thenReturn(Optional.of(officer));

        OfficerEnty result = officerService.findOfficer(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    // 4. Test for Officer Not Found (Negative Case)
    @Test
    void testFindOfficer_NotFound_ThrowsException() {
        when(officerRepo.findById("NONE")).thenReturn(Optional.empty());

        assertThrows(ResourcesNotFound.class, () -> {
            officerService.findOfficer("NONE");
        });
    }
}