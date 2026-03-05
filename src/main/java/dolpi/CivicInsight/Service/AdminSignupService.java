package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.DTO.userdto;
import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminSignupService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepo adminRepo;

    public String CreateUser(Admin admin){
        String encodepassword=passwordEncoder.encode(admin.getPassword());
        Admin user=new Admin();
        user.setName(admin.getName());
        user.setUsername(admin.getUsername());
        user.setPassword(encodepassword);
        user.setEmail(admin.getEmail());
        user.setRoles(Arrays.asList("ADMIN"));
        user.setCity(admin.getCity());
        adminRepo.save(user);

        return "SuccesFully Create User";
    }
}
