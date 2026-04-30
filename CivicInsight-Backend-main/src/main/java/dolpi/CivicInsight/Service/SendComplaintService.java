package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendComplaintService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private CompliantService compliantService;


    public String sendcomplaint(Complaints complaints) {
        complaints.setStatus("Processing");
        reportRepo.save(complaints);        // Give Id
        compliantService.processComplaint(complaints);       // work into background
        return "Complaint received! Tracking ID: " + complaints.getId();  //return id
    }
}
