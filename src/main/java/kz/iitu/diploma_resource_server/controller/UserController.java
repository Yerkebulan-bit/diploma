package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.EventFollowResult;
import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.UserToSave;
import kz.iitu.diploma_resource_server.register.AuthRegister;
import kz.iitu.diploma_resource_server.register.UserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRegister userRegister;
    private final AuthRegister authRegister;

    @Autowired
    public UserController(UserRegister userRegister, AuthRegister authRegister) {
        this.userRegister = userRegister;
        this.authRegister = authRegister;
    }

    @GetMapping("/load-user")
    public User loadUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        return userRegister.loadUserByNickName(auth.getName());
    }

    @GetMapping("/load-users")
    public List<User> loadUsers() {
        return userRegister.loadUsers();
    }

    @PostMapping("/follow-event")
    public EventFollowResult followEvent(@RequestParam("userId") String userId,
                                         @RequestParam("eventId") String eventId) {
        return userRegister.followEvent(userId, eventId);
    }

    @PostMapping("/unfollow-event")
    public void unfollowEvent(@RequestParam("userId") String userId,
                              @RequestParam("eventId") String eventId) {
        userRegister.unfollowEvent(userId, eventId);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserToSave user) {
        return userRegister.register(user);
    }

    @PostMapping("/mark-as-favorite")
    public void markAsFavorite(@RequestParam("userId") String userId,
                               @RequestParam("eventId") String eventId) {
        userRegister.markAsFavorite(userId, eventId);
    }

    @PostMapping("/unmark-as-favorite")
    public void unmarkAsFavorite(@RequestParam("userId") String userId,
                                 @RequestParam("eventId") String eventId) {
        userRegister.unmarkAsFavorite(userId, eventId);
    }

    @PostMapping("/disable")
    public void disableUser(@RequestParam("username") String username) {
        authRegister.disableUser(username);
    }

    @PostMapping("/enable")
    public void enableUser(@RequestParam("username") String username) {
        authRegister.enableUser(username);
    }

}
