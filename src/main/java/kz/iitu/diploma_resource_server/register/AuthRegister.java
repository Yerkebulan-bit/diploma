package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.UserType;

public interface AuthRegister {

    UserType identifyUserType(String username);

    String verifyCode(String codeId, String code);

    String sendVerificationCode(String username, String userId, String email);

    void disableUser(String username);

    void enableUser(String username);

}
