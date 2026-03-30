package dolpi.CivicInsight.Controller;

import dolpi.CivicInsight.Service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/google")
public class GoogleAuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    @GetMapping("/callback")
    public ResponseEntity<?> googleauth(
            @RequestParam String code,
            @RequestParam String check) {
        String jwtToken = googleAuthService.handleGoogleCallback(code, check);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }
}
