package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.register.EmailRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class MailController {

    private final EmailRegister emailRegister;

    @Autowired
    public MailController(EmailRegister emailRegister) {
        this.emailRegister = emailRegister;
    }

    @PostMapping("/subscribe")
    public void subscribeToMailing(@RequestParam("email") String email) {
        emailRegister.subscribeToMailing(email);
    }

    @PostMapping("/send-mailing")
    public void sendMassMailing(@RequestParam("message") String message,
                                @RequestParam("subject") String subject) {
        emailRegister.sendMassMailing(message, subject);
    }

}
