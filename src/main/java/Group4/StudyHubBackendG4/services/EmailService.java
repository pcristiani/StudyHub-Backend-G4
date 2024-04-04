package Group4.StudyHubBackendG4.services;
import Group4.StudyHubBackendG4.persistence.PasswordResetToken;
import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.PasswordResetTokenRepo;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

    public void forgotPassword(String email) {
        System.out.println("Email: " + email);
        User user = userRepo.findByEmail(email);
        System.out.println(user);
        String resetTokenLink = this.generatePasswordResetToken(user);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("studyhubg4@gmail.com");
        message.setTo(email);
        message.setSubject("StudyHub - Olvido de contraseña");
        message.setText("Haz click aquí para cambiar tu contraseña: " + resetTokenLink);

        mailSender.send(message);
    }

    public String generatePasswordResetToken(User user){
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exiration = now.plusMinutes(30L);         //30 minutos de expiracion
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(exiration);
        resetToken.setUser(user);
        PasswordResetToken token =  tokenRepo.save(resetToken);
        if(token != null) {
            String endpointUrl = "http://localhost:3000/resetPassword";
            return endpointUrl + "/" + resetToken.getToken();
        }
        return "";
    }

}
