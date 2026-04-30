package dolpi.CivicInsight.Controller;

import dolpi.CivicInsight.DTO.officerDTO;
import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Service.CompliantService;
import dolpi.CivicInsight.Service.SendComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

//This Controller is Complaint Controller

@RestController
@RequestMapping("/user/complaint")
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
//@CrossOrigin(origins = "http://127.0.0.1:5500")
public class Complaint {

    @Autowired
    private SendComplaintService sendComplaint;

    @Autowired
    private CompliantService compliantService;

    @PostMapping("/submit")
    public ResponseEntity<?> complaint(@RequestBody Complaints complaints){
      return new ResponseEntity<>(sendComplaint.sendcomplaint(complaints), HttpStatus.CREATED);
    }

    //user send id
    @GetMapping("/check/status")
    public ResponseEntity<?> complaint(@RequestParam("Id") String RegisterId){
        return new ResponseEntity<>(compliantService.checkstatus(RegisterId),HttpStatus.OK);
    }
}
