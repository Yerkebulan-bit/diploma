package kz.iitu.diploma_resource_server.register;

public interface EmailRegister {

    void subscribeToMailing(String email);

    void sendMassMailing(String message, String subject);

}
