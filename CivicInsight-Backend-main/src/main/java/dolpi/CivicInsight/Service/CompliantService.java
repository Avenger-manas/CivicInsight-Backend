package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.*;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import java.util.*;

@Service
@Slf4j
public class  CompliantService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private OfficerRepo officerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private  GroqService groqService;

    private final RestTemplate restTemplate = new RestTemplate();

    //this method perform task @Async
    @Async
    public void processComplaint(Complaints complaints) {

        GroqAnalysis analysis = groqService.analyzeComplaint(complaints.getComplaint());

        List<OfficerEnty> listofofficer = officerRepo.findByCityAndDepartment(
                complaints.getCity(), analysis.getDepartment()  // ← getDepartment()
        );

        if (listofofficer == null || listofofficer.isEmpty()) {
            log.error("No officer found for: {} - {}", complaints.getCity(), analysis.getDepartment());
            return;
        }

        for (OfficerEnty officer : listofofficer) {
            if (officer.getCountReport() < 5) {

                complaints.setUrgency(analysis.getUrgency());
                complaints.setStatus("Pending");
                complaints.setOfficerId(officer.getId());
                complaints.setOfficerName(officer.getName());
                complaints.setDepartmentName(analysis.getDepartment());

                officer.getReportIds().add(complaints.getId());
                officer.setCountReport(officer.getCountReport() + 1);

                officerRepo.save(officer);
                reportRepo.save(complaints);

                Optional<UserEnity> optionalUser = userRepo.findById(complaints.getUserId());
                if (optionalUser.isPresent()) {
                    UserEnity user = optionalUser.get();
                    emailService.sendEmail(
                            user.getEmail(),
                            "Complaint Successfully Submitted - CivicInsight",
                            "<h2>Hello " + user.getName() + "!</h2>" +
                                    "<p>Aapki complaint successfully submit ho gayi hai.</p>" +
                                    "<p><b>Complaint ID:</b> " + complaints.getId() + "</p>" +
                                    "<p><b>Complaint:</b> " + complaints.getComplaint() + "</p>" +
                                    "<p><b>Assigned Officer:</b> " + officer.getName() + "</p>" +
                                    "<p><b>Department:</b> " + analysis.getDepartment() + "</p>" +
                                    "<p><b>Category:</b> " + analysis.getCategory() + "</p>" +
                                    "<p><b>Summary:</b> " + analysis.getSummary() + "</p>" +
                                    "<p><b>Suggested Action:</b> " + analysis.getSuggested_Action() + "</p>" +
                                    "<p><b>City:</b> " + complaints.getCity() + "</p>" +
                                    "<p><b>Urgency:</b> " + analysis.getUrgency() + "</p>" +
                                    "<p><b>Status:</b> Pending </p>" +
                                    "<p>CivicInsight Team</p>"
                    );
                }
                break;
            }
        }
    }

    public String sendcomplaint(Complaints complaints) {
        complaints.setStatus("Processing");
        reportRepo.save(complaints);        // Give Id
        processComplaint(complaints);       // work into background
        return "Complaint received! Tracking ID: " + complaints.getId();  //return id
    }

    public Complaints checkstatus(String id) {
        Optional<Complaints> complaint = reportRepo.findById(id);
        if (!complaint.isPresent()) throw new ResourcesNotFound("NOT FOUND");
        return complaint.get();
    }
}
