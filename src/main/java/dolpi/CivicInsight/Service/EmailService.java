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
            helper.setText(body, true); // true = HTML support
            
            mailSender.send(message);
            System.out.println("Email sent to: " + to);
            
        } catch (MessagingException e) {
            throw new RuntimeException("Email send failed: " + e.getMessage());
        }
    }
}
