package Service;

import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Service.ComplaintSlaScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComplaintSlaSchedulerTest {

    @Mock
    private ReportRepo complaintRepository;

    @Mock
    private AdminRepo adminRepo;

    @InjectMocks
    private ComplaintSlaScheduler slaScheduler;

    @Test
    void testCheckSlaBreaches_ProcessingLogic() {
        // 1. Mock Data Setup
        Complaints mockComplaint = new Complaints();
        mockComplaint.setCity("Indore");
        mockComplaint.setUrgency("HIGH");

        Admin mockAdmin = new Admin();
        mockAdmin.setId("admin_789");
        mockAdmin.setCity("Indore");

        // 2. Mocking Repository Behavior
        when(complaintRepository.findByStatusAndUrgencyAndCreatedAtBeforeAndNotifiedFalse(
                eq("Pending"), eq("HIGH"), any(LocalDateTime.class)))
                .thenReturn(List.of(mockComplaint));

        // Jab MEDIUM aur LOW mangi jayein Empty return karwa dete hain
        when(complaintRepository.findByStatusAndUrgencyAndCreatedAtBeforeAndNotifiedFalse(
                eq("Pending"), argThat(s -> s.equals("MEDIUM") || s.equals("LOW")), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // City ke base par admin return karna
        when(adminRepo.findByCity("Indore")).thenReturn(mockAdmin);

        // 3. Method Execute Karein
        slaScheduler.checkSlaBreaches();

        // 4. Verifications

        verify(adminRepo, atLeastOnce()).findByCity("Indore");

        // Verify karo ki Complaint update hokar save hui
        verify(complaintRepository, atLeastOnce()).save(argThat(c ->
                c.getAdminId().equals("admin_789") &&
                        c.isNotified() == true &&
                        c.getAssignByAdmin().equals("Your Complaint Assigned By Admin")
        ));
    }
}