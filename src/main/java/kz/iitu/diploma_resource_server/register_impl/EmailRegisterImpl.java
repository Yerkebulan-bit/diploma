package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.email.EmailService;
import kz.iitu.diploma_resource_server.model.EmailId;
import kz.iitu.diploma_resource_server.register.EmailRegister;
import kz.iitu.diploma_resource_server.register.EventRegister;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.UUID;

@Service
public class EmailRegisterImpl implements EmailRegister {

    private final EmailService emailService;
    private final DataSource source;
    private final EventRegister eventRegister;

    @Autowired
    public EmailRegisterImpl(EmailService emailService, DataSource source, EventRegister eventRegister) {
        this.emailService = emailService;
        this.source = source;
        this.eventRegister = eventRegister;
    }

    @Override
    public void subscribeToMailing(String email) {
        SqlUpsert.into("mailing_list")
                .key("id", UUID.randomUUID().toString())
                .field("email", email)
                .toUpdate()
                .ifPresent(u -> u.applyTo(source));
    }

    @Override
    public void sendMassMailing(String message, String subject) {
        SqlSelectTo.theClass(EmailId.class)
                .sql("SELECT email, id FROM mailing_list")
                .applyTo(source)
                .forEach(
                        email -> emailService.sendEmail(message, subject, email.email)
                );

    }


}
