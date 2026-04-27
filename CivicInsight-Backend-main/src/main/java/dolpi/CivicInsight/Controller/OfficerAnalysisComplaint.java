package dolpi.CivicInsight.Controller;

import dolpi.CivicInsight.Service.OfficerAninalyisisComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department_officer/Analysis")
public class OfficerAnalysisComplaint {

    @Autowired
    private OfficerAninalyisisComplaintsService officerAninalyisisComplaintsService;

    @PostMapping("/fetch/complaint")
    public ResponseEntity<?> analysisComplaint(@RequestParam("Id") String Id) {
        return new ResponseEntity<>(officerAninalyisisComplaintsService.FetchComplaints(Id), HttpStatus.OK);
    }

    @PostMapping("/officer/complaint")
    public ResponseEntity<?> FetchOfficerComplaint(@RequestParam("Id") String officerId){
        return new ResponseEntity<>(officerAninalyisisComplaintsService.FetchComplaintOfficer(officerId), HttpStatus.OK);
    }

    @PostMapping("/analysis/complaint")
    public ResponseEntity<?> report(@RequestParam("Id") String id,@RequestParam("message") String reply){
        return new ResponseEntity<>(officerAninalyisisComplaintsService.AnalysisComplaint(id,reply),HttpStatus.OK);
    }

    @PostMapping("/find/Officer")
    public ResponseEntity<?> findOfficer(@RequestParam String Id){
        return new ResponseEntity<>(officerAninalyisisComplaintsService.findOfficer(Id),HttpStatus.OK);
    }
}