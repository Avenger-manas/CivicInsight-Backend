package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Controller.Complaint;
import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminFetchService {
    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private OfficerRepo officerRepo;

    public List<Complaints> fetchReport(String id){
        Optional<Admin> optionaladmin=adminRepo.findById(id);
        Admin admin=optionaladmin.get();
        if(admin==null) throw new ResourcesNotFound("Admin is Null");
        List<Complaints>listofcomplaint=reportRepo.findByCity(admin.getCity());
        return listofcomplaint;
    }

    public List<OfficerEnty> findAllOfficer(String Id){
        Optional<Admin> optionaladmin=adminRepo.findById(Id);
        Admin admin=optionaladmin.get();

        if(admin==null) throw new ResourcesNotFound("Admin is Null");
        List<OfficerEnty>officerList=officerRepo.findByCity(admin.getCity());
        if(officerList==null) throw new ResourcesNotFound("Officer NOT Found");

        return officerList;

    }

    public List<Complaints> FetchAllUnsolvedComplaints(String id){
        Optional<Admin> optionalAdmin=adminRepo.findById(id);

        if(!optionalAdmin.isPresent()) throw new ResourcesNotFound("Admin Is Null");
        Admin admin=optionalAdmin.get();

        List<Complaints>complaintsList=reportRepo.findByAdminIdAndStatusAndNotified(admin.getId(),"Pending",true);

        if(complaintsList==null) throw new ResourcesNotFound("Complaints Not Found");

        return complaintsList;
    }

}
