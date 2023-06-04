package kz.iitu.diploma_resource_server.email;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceReal implements EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceReal(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @SneakyThrows
    @Override
    public void sendEmail(String message, String subject, String email) {
        SimpleMailMessage messageObject = new SimpleMailMessage();
        messageObject.setFrom("noreply@cityevent.com");
        messageObject.setTo(email);
        messageObject.setSubject(subject);
        messageObject.setText(message);
        mailSender.send(messageObject);
    }

}
