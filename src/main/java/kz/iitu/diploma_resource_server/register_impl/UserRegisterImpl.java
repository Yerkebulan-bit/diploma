package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.exception.EventLimitReachedException;
import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.UserToSave;
import kz.iitu.diploma_resource_server.register.EventRegister;
import kz.iitu.diploma_resource_server.register.UserRegister;
import kz.iitu.diploma_resource_server.sql.EventTable;
import kz.iitu.diploma_resource_server.sql.UserTable;
import kz.iitu.diploma_resource_server.util.StringUtils;
import kz.iitu.diploma_resource_server.util.Strings;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class UserRegisterImpl implements UserRegister {

    private final DataSource source;
    private final PasswordEncoder passwordEncoder;
    private final EventRegister eventRegister;

    @Autowired
    public UserRegisterImpl(DataSource source, PasswordEncoder passwordEncoder, EventRegister eventRegister) {
        this.source = source;
        this.passwordEncoder = passwordEncoder;
        this.eventRegister = eventRegister;
    }

    @Override
    public void followEvent(String userId, String eventId) {
        if (StringUtils.isNullOrEmpty(userId)) {
            throw new RuntimeException("es4dn6gdd");
        }

        if (StringUtils.isNullOrEmpty(eventId)) {
            throw new RuntimeException("ds88gfSa");
        }

        var event = eventRegister.loadEventById(eventId);

        if (event.current >= event.limit) {
            throw new EventLimitReachedException("fd@edos88");
        }

        SqlUpsert.into("user_event")
                .key("id", UUID.randomUUID().toString())
                .field("user_id", userId)
                .field("event_id", eventId)
                .toUpdate().ifPresent(u -> u.applyTo(source));

        SqlUpsert.into(EventTable.TABLE_NAME)
                .key(EventTable.ID, eventId)
                .field(EventTable.CURRENT, event.current++)
                .toUpdate().ifPresent(u -> u.applyTo(source));
    }

    @Override
    public void unfollowEvent(String userId, String eventId) {
        Strings.requiresNotNullOrEmpty(userId);
        Strings.requiresNotNullOrEmpty(eventId);

        var event = eventRegister.loadEventById(eventId);

        try (var con = source.getConnection();
             var ps = con.prepareStatement("DELETE FROM diploma.user_event WHERE user_id = ? AND event_id = ?")) {
            ps.setString(1, userId);
            ps.setString(2, eventId);

            ps.executeUpdate();

            SqlUpsert.into(EventTable.TABLE_NAME)
                    .key(EventTable.ID, eventId)
                    .field(EventTable.CURRENT, event.current--)
                    .toUpdate().ifPresent(u -> u.applyTo(con));
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    @Override
    public User loadUserByNickName(String nickname) {
        Strings.requiresNotNullOrEmpty(nickname);

        return SqlSelectTo.theClass(User.class)
                .sql(UserTable.SELECT_USER_BY_NICKNAME)
                .param(nickname)
                .applyTo(source)
                .stream().findFirst().orElseThrow();
    }

    @Override
    public List<User> loadUsers() {
        return SqlSelectTo.theClass(User.class)
                .sql(UserTable.SELECT_USERS)
                .applyTo(source);
    }

    @SneakyThrows
    @Override
    public String register(UserToSave user) {
        Objects.requireNonNull(user);

        Strings.requiresNotNullOrEmpty(user.username);
        Strings.requiresNotNullOrEmpty(user.rawPassword);
        Strings.requiresNotNullOrEmpty(user.name);
        Strings.requiresNotNullOrEmpty(user.email);

        var userId = StringUtils.isNullOrEmpty(user.id) ? UUID.randomUUID().toString() : user.id;

        try (var connection = source.getConnection()) {
            SqlUpsert.into(UserTable.TABLE_NAME)
                    .key(UserTable.ID, userId)
                    .field(UserTable.NAME, user.name)
                    .field(UserTable.SURNAME, user.surname)
                    .field(UserTable.BIRTH, user.birth)
                    .field(UserTable.EMAIL, user.email)
                    .field(UserTable.ABOUT, user.about)
                    .field(UserTable.PHONE, user.phone)
                    .field(UserTable.IMAGE_ID, user.imageId)
                    .field(UserTable.USERNAME, user.username)
                    .toUpdate()
                    .ifPresent(u -> u.applyTo(connection));

            SqlUpsert.into("users")
                    .key("username", user.username)
                    .field("password", passwordEncoder.encode(user.rawPassword))
                    .field("enabled", true)
                    .toUpdate()
                    .ifPresent(u -> u.applyTo(connection));

            SqlUpsert.into("authorities")
                    .key("username", user.username)
                    .field("authority", "read")
                    .toUpdate()
                    .ifPresent(u -> u.applyTo(connection));
        }

        return userId;
    }

    @SneakyThrows
    @Override
    public void markAsFavorite(String userId, String eventId) {
        SqlUpsert.into("favorite_event")
                .key("id", UUID.randomUUID().toString())
                .field("event_id", eventId)
                .field("user_id", userId)
                .toUpdate()
                .ifPresent(u -> u.applyTo(source));
    }

    @Override
    @SneakyThrows
    public void unmarkAsFavorite(String userId, String eventId) {
        try (var con = source.getConnection();
             var ps = con.prepareStatement("DELETE FROM diploma.favorite_event WHERE user_id = ? AND event_id = ?")) {
            ps.setString(1, userId);
            ps.setString(2, eventId);

            ps.executeUpdate();
        }
    }

}
