package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OfficerAninalyisisComplaintsService {

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private OfficerRepo officerRepo;

    public List<Complaints> FetchComplaintOfficer(String officerId){
         List<Complaints>listComplaints=reportRepo.findByOfficerId(officerId);
         if(listComplaints==null || listComplaints.isEmpty()) throw new ResourcesNotFound("NOT FOUND");
         return listComplaints;
    }

    public Complaints FetchComplaints(String Id){
        Optional<Complaints> listComplaints=reportRepo.findById(Id);
        if(!listComplaints.isPresent()) throw new ResourcesNotFound("NOT FOUND");
        return listComplaints.get();
    }

    public String AnalysisComplaint(String Id,String reply){
        Optional<Complaints> optionalComplaint = reportRepo.findById(Id);

        if (optionalComplaint.isPresent()) {
            Complaints complaint = optionalComplaint.get();

            // . List check karein
            if (complaint.getListReport() == null || complaint.getListReport().isEmpty()) {
                complaint.setListReport(new ArrayList<>());
               // throw new ResourcesNotFound("NO Available Complaint");
            }

            Optional<OfficerEnty> optionalofficer=officerRepo.findById(complaint.getOfficerId());
            if(!optionalofficer.isPresent()) throw new ResourcesNotFound("Officer Not Handle Report");

            OfficerEnty officer=optionalofficer.get();
            officer.setCountReport(officer.getCountReport()-1);

            complaint.getListReport().add(reply);


            complaint.setStatus("Analyzed");

            officerRepo.save(officer);
            reportRepo.save(complaint);

            return "Complaint updated successfully!";
        } else {
            return "Complaint not found with ID: " + Id;
        }

    }

    public OfficerEnty findOfficer(String Id){
        Optional<OfficerEnty>optionalOfficer=officerRepo.findById(Id);

        if(!optionalOfficer.isPresent()) throw new ResourcesNotFound("Officer Null");

        OfficerEnty officerEnty=optionalOfficer.get();
        return officerEnty;
    }
}
