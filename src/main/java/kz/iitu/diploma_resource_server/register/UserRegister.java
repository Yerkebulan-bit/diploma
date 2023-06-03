package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.EventFollowResult;
import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.UserToSave;

import java.util.List;

public interface UserRegister {

    EventFollowResult followEvent(String userId, String eventId);

    void unfollowEvent(String userId, String eventId);

    User loadUserByNickName(String nickname);

    List<User> loadUsers();

    String register(UserToSave user);

    void markAsFavorite(String userId, String eventId);

    void unmarkAsFavorite(String userId, String eventId);

}

