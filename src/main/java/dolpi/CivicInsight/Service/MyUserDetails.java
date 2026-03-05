package dolpi.CivicInsight.Service;

import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Repository.AdminRepo;
import dolpi.CivicInsight.Repository.OfficerRepo;
import dolpi.CivicInsight.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {
    @Autowired
    private UserRepo userRep;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private OfficerRepo officerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEnity user = userRep.findByUsername(username);
        if (user != null) {

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }

        Admin admin=adminRepo.findByUsername(username);
        if(admin!=null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(admin.getUsername())
                    .password(admin.getPassword())
                    .roles(admin.getRoles().toArray(new String[0]))
                    .build();
        }

        OfficerEnty officer=officerRepo.findByUsername(username);

        if(officer!=null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(officer.getUsername())
                    .password(officer.getPassword())
                    .roles(officer.getRoles().toArray(new String[0]))
                    .build();
        }

        throw new UsernameNotFoundException("No user or hospital found with username: " + username);


    }
}
