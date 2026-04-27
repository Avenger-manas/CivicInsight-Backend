package dolpi.CivicInsight.DTO;

import dolpi.CivicInsight.Entity.UserEnity;
import dolpi.CivicInsight.Exception.ResourcesNotFound;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class AdminDTO {
    private String username;

    private String password;
}
