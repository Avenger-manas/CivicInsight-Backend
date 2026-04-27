package dolpi.CivicInsight.Controller;

import dolpi.CivicInsight.Service.AdminFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/fetch")
public class AdminFetchAllComplaint {

    @Autowired
    private AdminFetchService adminFetchService;
    @PostMapping("/reports")
    public ResponseEntity<?> fetchAllComplaint(@RequestParam("Id") String Id){
       return new ResponseEntity<>(adminFetchService.fetchReport(Id), HttpStatus.OK);
    }

    @PostMapping("/find/AllOfficer")
    public ResponseEntity<?> findAllOfficer(@RequestParam("Id") String Id){
        return new ResponseEntity<>(adminFetchService.findAllOfficer(Id),HttpStatus.OK);
    }

    @PostMapping("/unsolved/complaint")
    public ResponseEntity<?> FetchUnsolvedComplaint(@RequestParam String Id){
        return new ResponseEntity<>(adminFetchService.FetchAllUnsolvedComplaints(Id),HttpStatus.OK);
    }
}
