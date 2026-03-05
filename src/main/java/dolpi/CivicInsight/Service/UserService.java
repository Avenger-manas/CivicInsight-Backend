package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.DTO.userdto;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    public String CreateUser(userdto userdto){
        String encodepassword=passwordEncoder.encode(userdto.getPassword());
        UserEnity user=new UserEnity();
        user.setName(userdto.getName());
        user.setUsername(userdto.getUsername());
        user.setPassword(encodepassword);
        user.setEmail(userdto.getEmail());
        user.setRoles(Arrays.asList("USER"));
        userRepo.save(user);

        return "SuccesFully Create User";
    }
}
