package Group4.StudyHubBackendG4.services;
import Group4.StudyHubBackendG4.persistence.PasswordResetToken;
import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordResetTokenRepo tokenRepo;

    public ResponseEntity<?> forgotPassword(String email) throws MessagingException, IOException {
        if(userRepo.existsByEmail(email)){
            User user = userRepo.findByEmail(email);
            String username = user.getUsername();
            String resetTokenLink = this.generatePasswordResetToken(user);

            Resource resource = new ClassPathResource("forgotMail.html");
            byte[] htmlContentBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String htmlContent = new String(htmlContentBytes, StandardCharsets.UTF_8);

            htmlContent = htmlContent.replace("$username", username);
            htmlContent = htmlContent.replace("$resetTokenLink", resetTokenLink);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("studyhubg4@gmail.com");
            helper.setTo(email);
            helper.setSubject("StudyHub - Olvido de contraseña");

            helper.setText(htmlContent, true);

            mailSender.send(message);

            return ResponseEntity.ok().body("Email enviado.");
        }
        return ResponseEntity.badRequest().body("Invalid email.");
    }

    public String generatePasswordResetToken(User user){
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusMinutes(30L);         //30 minutos de expiracion
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiration);
        resetToken.setUser(user);
        PasswordResetToken token =  tokenRepo.save(resetToken);
        if(token != null) {
            String endpointUrl = "http://localhost:3000/resetPassword";
            return endpointUrl + "/?token=" + resetToken.getToken();
        }
        return "";
    }

}
