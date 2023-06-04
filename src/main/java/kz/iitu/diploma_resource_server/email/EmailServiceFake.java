package kz.iitu.diploma_resource_server.email;

public class EmailServiceFake implements EmailService {

    @Override
    public void sendEmail(String code, String subject, String toMail) {
        System.out.println(code + subject + toMail);
    }

}
