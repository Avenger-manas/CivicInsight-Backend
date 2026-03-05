package Service;

import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Service.AdminFetchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AdminFetchServiceTest {

    @Mock
    private AdminRepo adminRepo;

    @Mock
    private ReportRepo reportRepo;

    @Mock
    private OfficerRepo officerRepo;

    @InjectMocks
    private AdminFetchService adminFetchService;

    private Admin mockAdmin;

    @BeforeEach
    void setUp() {
        mockAdmin = new Admin();
        mockAdmin.setId("admin123");
        mockAdmin.setCity("Indore");
    }

    // 1. Test for fetchReport
    @Test
    void testFetchReport_Success() {
        Mockito.when(adminRepo.findById("admin123")).thenReturn(Optional.of(mockAdmin));
        Mockito.when(reportRepo.findByCity("Indore")).thenReturn(Arrays.asList(new Complaints(), new Complaints()));

        List<Complaints> result = adminFetchService.fetchReport("admin123");

        assertEquals(2, result.size());
        Mockito.verify(reportRepo).findByCity("Indore");
    }

    // 2. Test for findAllOfficer
    @Test
    void testFindAllOfficer_Success() {
        Mockito.when(adminRepo.findById("admin123")).thenReturn(Optional.of(mockAdmin));
        Mockito.when(officerRepo.findByCity("Indore")).thenReturn(Arrays.asList(new OfficerEnty()));

        List<OfficerEnty> result = adminFetchService.findAllOfficer("admin123");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // 3. Test for FetchAllUnsolvedComplaints
    @Test
    void testFetchAllUnsolvedComplaints_Success() {
        Mockito.when(adminRepo.findById("admin123")).thenReturn(Optional.of(mockAdmin));

        List<Complaints> mockComplaints = Arrays.asList(new Complaints());
        Mockito.when(reportRepo.findByAdminIdAndStatusAndNotified("admin123", "Pending", true))
                .thenReturn(mockComplaints);

        List<Complaints> result = adminFetchService.FetchAllUnsolvedComplaints("admin123");

        assertEquals(1, result.size());
    }

    // 4. Test for Exception (Negative Scenario)
    @Test
    void testFetchAllUnsolvedComplaints_AdminNotFound() {
        Mockito.when(adminRepo.findById("invalid_id")).thenReturn(Optional.empty());

        assertThrows(ResourcesNotFound.class, () -> {
            adminFetchService.FetchAllUnsolvedComplaints("invalid_id");
        });
    }
}