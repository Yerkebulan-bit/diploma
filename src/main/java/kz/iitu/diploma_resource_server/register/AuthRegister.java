package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.UserType;

public interface AuthRegister {

    UserType identifyUserType(String username);

}
