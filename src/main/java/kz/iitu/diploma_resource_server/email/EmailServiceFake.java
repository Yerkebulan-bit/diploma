package kz.iitu.diploma_resource_server.email;

public class EmailServiceFake implements EmailService {

    @Override
    public void sendEmail(String code, String toMail) {
        System.out.println(code);
    }

}
