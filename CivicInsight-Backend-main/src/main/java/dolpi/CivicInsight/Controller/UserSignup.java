package dolpi.CivicInsight.Controller;

import dolpi.CivicInsight.DTO.UserLogin;
import dolpi.CivicInsight.DTO.userdto;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import dolpi.CivicInsight.JwtToken.jwttoken;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import dolpi.CivicInsight.Service.MyUserDetails;
import dolpi.CivicInsight.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/create")
public class UserSignup {
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";


    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private OfficerRepo officerRepo;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetails myUserDetails;

    @Autowired
    private jwttoken jwttoken;

    @PostMapping("/signup/user")
    public ResponseEntity<?> signup(@RequestBody userdto userdto){

        boolean check=pattern.matcher(userdto.getEmail()).matches();

        //check the user exits are not uniq username

        if(!check)  throw new ResourcesNotFound("User Email Incorrect Please Enter Valid Email");

        if(officerRepo.findByUsername(userdto.getUsername())!=null) throw new ResourcesNotFound("Try Uniq UserName");

        if(adminRepo.findByUsername(userdto.getUsername())!=null) throw new ResourcesNotFound("Try Uniq UserName");

        if(adminRepo.existsByEmail(userdto.getEmail())) throw new ResourcesNotFound("This User Is Already Exits");

        if(officerRepo.existsByEmail(userdto.getEmail())) throw new ResourcesNotFound("This User Is Already Exits");

        if(userRepo.findByUsername(userdto.getUsername())!=null){
            throw new ResourcesNotFound("Create Uniqe Username");
        }

        if(userRepo.existsByEmail(userdto.getEmail())){
            throw new ResourcesNotFound("This User is Already Exits");
        }

        return new ResponseEntity<>(userService.CreateUser(userdto),HttpStatus.CREATED);

    }

    @PostMapping("/login/user")
    public ResponseEntity<?> loginuser(@RequestBody UserLogin userLogin){
        UserEnity user=userRepo.findByUsername(userLogin.getUsername());
        if(user.getRoles() == null || !user.getRoles().contains("USER")) return new ResponseEntity<> ("Incorrect User",HttpStatus.BAD_REQUEST);

        if(user==null) throw new ResourcesNotFound("Please Enter Valid Username ");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
        UserDetails userDetails = myUserDetails.loadUserByUsername(userLogin.getUsername());

        String jwt = jwttoken.generateToken(userLogin.getUsername());

        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }
}
