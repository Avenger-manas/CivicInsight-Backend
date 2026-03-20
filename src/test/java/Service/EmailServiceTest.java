package Service;

import dolpi.CivicInsight.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendEmail_Success() throws MessagingException {
        //  Mock MimeMessage
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Call the service method
        emailService.sendEmail(
            "test@example.com",
            "Test Subject",
            "<h1>Test Body</h1>"
        );

        //  Verification
        // Check if createMimemessage was called
        verify(mailSender, times(1)).createMimeMessage();
        //check if send was called
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmail_Failure() {
        // Mock MimeMessage ko null return karwao exception trigger hogi
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Mail server down"));

        // Verify ki RuntimeException throw hoti hai
        assertThrows(RuntimeException.class, () -> {
            emailService.sendEmail(
                "test@example.com",
                "Test Subject",
                "<h1>Test Body</h1>"
            );
        });
    }
}
