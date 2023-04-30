package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.UserToSave;

public interface UserRegister {

    void followEvent(String userId, String eventId);

    void unfollowEvent(String userId, String eventId);

    User loadUserByNickName(String nickname);

    String register(UserToSave user);

    void markAsFavorite(String userId, String eventId);

    void unmarkAsFavorite(String userId, String eventId);

}

