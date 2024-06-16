package Group4.StudyHubBackendG4.services;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.repositories.UsuarioRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UsuarioRepo usuarioRepo;

    @Autowired
    PasswordResetTokenRepo tokenRepo;

    public ResponseEntity<?> sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        if(usuarioRepo.existsByEmail(to)){
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("studyhubg4@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            return ResponseEntity.ok().body("Email enviado.");
        }
        return ResponseEntity.badRequest().body("Invalid email.");
    }

    public String getHtmlContent(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        byte[] htmlContentBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(htmlContentBytes, StandardCharsets.UTF_8);
    }

}
