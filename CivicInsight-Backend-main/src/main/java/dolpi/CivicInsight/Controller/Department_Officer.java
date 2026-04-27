package dolpi.CivicInsight.Controller;

import dolpi.CivicInsight.DTO.OfficerLogin;
import dolpi.CivicInsight.DTO.UserLogin;
import dolpi.CivicInsight.DTO.officerDTO;
import dolpi.CivicInsight.DTO.userdto;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.JwtToken.jwttoken;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import dolpi.CivicInsight.Service.MyUserDetails;
import dolpi.CivicInsight.Service.OfficerService;
import dolpi.CivicInsight.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/create/department_user")
public class Department_Officer {
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";


    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OfficerRepo officerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private MyUserDetails myUserDetails;

    @Autowired
    private jwttoken jwttoken;

    @Autowired
    private OfficerService officerService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupofficer(@RequestBody officerDTO officerDTO){
        boolean check=pattern.matcher(officerDTO.getEmail()).matches();

        if(!check)  throw new ResourcesNotFound("User Email Incorrect Please Enter Valid Email");

        if(officerRepo.findByUsername(officerDTO.getUsername())!=null){
            throw new ResourcesNotFound("Create Uniqe Username");
        }

        if(userRepo.findByUsername(officerDTO.getUsername())!=null) throw new ResourcesNotFound("Try Uniq UserName");

        if(userRepo.existsByEmail(officerDTO.getEmail())) throw new ResourcesNotFound("This User Is Already Exits");

        if(adminRepo.findByUsername(officerDTO.getUsername())!=null) throw new ResourcesNotFound("Try Uniq UserName");

        if(adminRepo.existsByEmail(officerDTO.getEmail())) throw new ResourcesNotFound("This User Is Already Exits");


        if(officerRepo.existsByEmail(officerDTO.getEmail())){
            throw new ResourcesNotFound("This User is Already Exits");
        }

        return new ResponseEntity<>(officerService.Createofficer(officerDTO),HttpStatus.CREATED);
    }

    @PostMapping("/login/officer")
    public ResponseEntity<?> logiofficer(@RequestBody OfficerLogin officerlogin){
        OfficerEnty officer=officerRepo.findByUsername(officerlogin.getUsername());
        if(officer.getRoles() == null || !officer.getRoles().contains("OFFICER")) throw new ResourcesNotFound("Incorrect User");

        if(officer==null)  throw new ResourcesNotFound("Please Enter Valid Username ");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(officerlogin.getUsername(), officerlogin.getPassword()));
        UserDetails userDetails = myUserDetails.loadUserByUsername(officerlogin.getUsername());

        String jwt = jwttoken.generateToken(officerlogin.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("id", officer.getId()); // Ab yahan se 69a26358... jayegi
        response.put("username", officer.getUsername());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

