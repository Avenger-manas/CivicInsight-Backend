package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Controller.Complaint;
import dolpi.CivicInsight.Entity.ComplaintAnalysis;
import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.OllamaResponse;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.MediaType;

import java.util.*;

@Service
public class CompliantService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private OfficerRepo officerRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendcomplaint(Complaints complaints) {
        String url = "https://deciduously-unled-delila.ngrok-free.dev/analyze_letter";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("letter", complaints.getComplaint());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // 1. OllamaResponse class
        OllamaResponse response = restTemplate.postForObject(url, entity, OllamaResponse.class);


        //send detals officer
        List<OfficerEnty>listofofficer=officerRepo.findByCityAndDepartment(complaints.getCity(),response.getAnalysis().getDepartment());
        String OfficerName="";
        String DepartmentName="";

        if(listofofficer==null) throw new ResourcesNotFound("Officer Not Found");

        for(OfficerEnty officer:listofofficer){
            if(officer.getCountReport()<5){
                OfficerName=officer.getName();
                DepartmentName=officer.getDepartment();
                complaints.setUrgency(response.getAnalysis().getUrgency());
                complaints.setStatus("Pending");
                complaints.setOfficerId(officer.getId());
                complaints.setOfficerName(officer.getName());
                complaints.setDepartmentName(officer.getDepartment());
                officer.getReportIds().add(complaints.getId());
                officer.setCountReport(officer.getCountReport()+1);
                officerRepo.save(officer);
                reportRepo.save(complaints);
                break;
            }
        }

        return "Your Report Is Submitted By "+OfficerName+" Department Name "+DepartmentName;

    }

    public Complaints checkstatus(String id){
         Optional<Complaints> complaint=reportRepo.findById(id);
        if(!complaint.isPresent()) throw new ResourcesNotFound("NOT FOUND");
        return complaint.get();
    }
}
