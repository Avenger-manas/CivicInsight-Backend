package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintSlaScheduler {

    @Autowired
    private ReportRepo complaintRepository;

    @Autowired
    private AdminRepo adminrepo;

    @Scheduled(fixedRate = 1800000)
    public void checkSlaBreaches() {

        LocalDateTime now = LocalDateTime.now();

        processUrgency("HIGH", now.minusHours(24));
        processUrgency("MEDIUM", now.minusDays(3));
        processUrgency("LOW",  now.minusWeeks(7));
    }

    private void processUrgency(String urgency,
                                LocalDateTime threshold) {

        List<Complaints> complaints =
                complaintRepository
                        .findByStatusAndUrgencyAndCreatedAtBeforeAndNotifiedFalse(
                                "Pending",
                                urgency,
                                threshold
                        );

        System.out.println("Urgency: " + urgency);
        System.out.println("Found: " + complaints.size());

        for (Complaints complaint : complaints) {

            // 🔥 City ke base par admin nikalna
            Admin admin = adminrepo.findByCity(complaint.getCity());

            if (admin != null) {

                complaint.setAdminId(admin.getId());
                complaint.setAssignByAdmin("Your Complaint Assigned By Admin");
                complaint.setNotified(true);

                complaintRepository.save(complaint);
            }
        }
    }
}