package Group4.StudyHubBackendG4.util;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

@Component
public class DummyJavaMailSender implements JavaMailSender {

    @Override
    public MimeMessage createMimeMessage() {
        return new DummyMimeMessage(getSession());
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return new DummyMimeMessage(getSession());
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        // Mocking sending logic for MimeMessage
        System.out.println("Dummy JavaMailSender: Sending email...");
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {
        for (MimeMessage mimeMessage : mimeMessages) {
            send(mimeMessage);
        }
    }

    // Method to obtain a mock Session instance
    private Session getSession() {
        Properties properties = new Properties();
        // Customize properties as needed
        return Session.getDefaultInstance(properties);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        for (SimpleMailMessage simpleMessage : simpleMessages) {
            send(simpleMessage);
        }
    }

    // Inner class to simulate MimeMessage
    private static class DummyMimeMessage extends MimeMessage {

        public DummyMimeMessage(Session session) {
            super(session);
        }
    }
}
