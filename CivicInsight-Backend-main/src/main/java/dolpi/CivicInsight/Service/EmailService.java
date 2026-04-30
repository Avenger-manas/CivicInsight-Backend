package dolpi.CivicInsight.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
            helper.setFrom("manasrastogi64@gmail.com"); 
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); 
            
            mailSender.send(message);
            System.out.println("Email successfully sent to: " + to);
            
        } catch (MessagingException e) {
            e.printStackTrace(); 
            throw new RuntimeException("Email delivery failed: " + e.getMessage());
        }
    }
}
