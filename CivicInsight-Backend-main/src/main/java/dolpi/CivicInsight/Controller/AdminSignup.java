package dolpi.CivicInsight.Controller;


import dolpi.CivicInsight.DTO.AdminDTO;
import dolpi.CivicInsight.DTO.UserLogin;
import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.JwtToken.jwttoken;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import dolpi.CivicInsight.Service.AdminSignupService;
import dolpi.CivicInsight.Service.MyUserDetails;
import dolpi.CivicInsight.Service.OfficerService;
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
@RequestMapping("/admin/signup")
public class AdminSignup {
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";


    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private OfficerRepo officerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MyUserDetails myUserDetails;

    @Autowired
    private jwttoken jwttoken;

    @Autowired
    private AdminSignupService adminSignupService;

    @PostMapping("/crateAdmin")
    public ResponseEntity<?> craeteUser(@RequestBody Admin admin){
        boolean check=pattern.matcher(admin.getEmail()).matches();

        if(!check)  throw new ResourcesNotFound("User Email Incorrect Please Enter Valid Email");

        if(officerRepo.findByUsername(admin.getUsername())!=null) throw new ResourcesNotFound("Try Uniq UserName");

        if(officerRepo.existsByEmail(admin.getEmail())) throw new ResourcesNotFound("This User Is Already Exits");

        if(userRepo.findByUsername(admin.getUsername())!=null) throw new ResourcesNotFound("Try Uniq UserName");

        if(userRepo.existsByEmail(admin.getEmail())) throw new ResourcesNotFound("This User Is Already Exits");


        if(adminRepo.findByUsername(admin.getUsername())!=null){
            throw new ResourcesNotFound("Create Uniqe Username");
        }

        if(adminRepo.existsByEmail(admin.getEmail())){
            throw new ResourcesNotFound("This User is Already Exits");
        }

        return new ResponseEntity<>(adminSignupService.CreateUser(admin), HttpStatus.CREATED);
    }

//    @PostMapping("/login/admin")
//    public ResponseEntity<?> loginuser(@RequestBody AdminDTO adminDTO){
//        Admin user=adminRepo.findByUsername(adminDTO.getUsername());
//        if(user.getRoles().equals("ADMIN")) return new ResponseEntity<> ("Incorrect User",HttpStatus.BAD_REQUEST);
//
//        if(user==null) throw new ResourcesNotFound("Please Enter Valid Username ");
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(adminDTO.getUsername(), adminDTO.getPassword()));
//        UserDetails userDetails = myUserDetails.loadUserByUsername(adminDTO.getUsername());
//
//        String jwt = jwttoken.generateToken(user.getUsername());
//
//        return new ResponseEntity<>(jwt, HttpStatus.OK);
//    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> loginuser(@RequestBody AdminDTO adminDTO) {
        Admin user = adminRepo.findByUsername(adminDTO.getUsername());

        // 1. Check if user exists
        if (user == null) throw new ResourcesNotFound("Please Enter Valid Username");

        // 2. Role Check (Fix: Agar role ADMIN NAHI hai
        if (user.getRoles() == null || !user.getRoles().contains("ADMIN")) {
            return new ResponseEntity<>("Incorrect User: Not an Admin", HttpStatus.BAD_REQUEST);
        }

        // 3. Authenticate
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(adminDTO.getUsername(), adminDTO.getPassword()));

        // 4. Generate Token
        String jwt = jwttoken.generateToken(user.getUsername());

        // 5. Build Response Object (Map use karein taaki Token aur ID dono jayein)
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("id", user.getId()); // Ab yahan se 69a26358... jayegi
        response.put("username", user.getUsername());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
