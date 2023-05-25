package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.model.Rating;
import kz.iitu.diploma_resource_server.model.SearchFilter;
import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.event.*;
import kz.iitu.diploma_resource_server.register.EventRegister;
import kz.iitu.diploma_resource_server.register.OrganizationRegister;
import kz.iitu.diploma_resource_server.register.UserRegister;
import kz.iitu.diploma_resource_server.sql.EventTable;
import kz.iitu.diploma_resource_server.sql.SqlBuilder;
import kz.iitu.diploma_resource_server.sql.UserTable;
import kz.iitu.diploma_resource_server.util.StringUtils;
import kz.iitu.diploma_resource_server.util.Strings;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class EventRegisterImpl implements EventRegister {

    private final DataSource source;
    private final OrganizationRegister organizationRegister;
    private final UserRegister userRegister;

    private static final String AVG_COLUMN_NAME = "average";

    private static final String COUNT_AVG_RATING = "SELECT AVG(rating) as " + AVG_COLUMN_NAME + " FROM event_rating WHERE event_id = ?";

    private static final String USER_RATING = "SELECT rating FROM event_rating WHERE event_id = ? AND user_id = ?";

    @Autowired
    public EventRegisterImpl(DataSource source, OrganizationRegister organizationRegister, @Lazy UserRegister userRegister) {
        this.source = source;
        this.organizationRegister = organizationRegister;
        this.userRegister = userRegister;
    }

    @Override
    public String saveEvent(EventToSave event) {

        if (StringUtils.isNullOrEmpty(event.organizationId)) {
            throw new RuntimeException("gr112dwW");
        }

        if (StringUtils.isNullOrEmpty(event.id)) {
            event.id = UUID.randomUUID().toString();
        }

        SqlUpsert.into(EventTable.TABLE_NAME)
                .key(EventTable.ID, event.id)
                .field(EventTable.NAME, event.name)
                .field(EventTable.TYPE, event.type)
                .field(EventTable.DESCRIPTION, event.description)
                .field(EventTable.LOCATION, event.location)
                .field(EventTable.ENDED_AT, event.endedAt)
                .field(EventTable.ORG, event.organizationId)
                .field(EventTable.SHORT_DESC, event.shortDescription)
                .field(EventTable.TIME, event.time)
                .field(EventTable.RUNNING_TIME, event.runningTime)
                .field(EventTable.STARTED_AT, event.startedAt)
                .field(EventTable.CONSTRAINTS, event.constraints)
                .field(EventTable.IMAGE_ID, event.imageId)
                .field(EventTable.DAY, event.day.name())
                .field(EventTable.YT_URL, event.ytUrl)
                .toUpdate()
                .ifPresent(u -> u.applyTo(source));

        return event.id;
    }

    @Override
    public List<Event> loadMainEvents() {
        return loadEvents(SearchFilter.onlyMain(), Day.ALL);
    }

    @Override
    public List<Event> loadWeekEvents() {
        return loadEvents(SearchFilter.currentWeak(), Day.ALL);
    }

    @SneakyThrows
    @Override
    public List<Event> loadEvents(SearchFilter filter, Day day) {
        var sql = SqlBuilder.eventSelectByFilter(filter, day);

        var events = SqlSelectTo.theClass(Event.class)
                .sql(sql)
                .applyTo(source);

        try (var connection = source.getConnection()) {
            for (var event : events) {

                try {
                    event.rating = countRating(connection, event.id, 1);
                } catch (Exception e) {
                    event.rating = 1;
                }

            }
        }

        return events;
    }

    @Override
    public List<Event> loadOrgEvents(String orgId) {

        if (StringUtils.isNullOrEmpty(orgId)) {
            throw new RuntimeException("g545EF8F");
        }

        var sql = EventTable.SELECT_EVENTS_BY_ORG_ID;

        return SqlSelectTo.theClass(Event.class)
                .sql(sql)
                .param(orgId)
                .applyTo(source);
    }

    @Override
    public Event loadEventById(String eventId) {
        Strings.requiresNotNullOrEmpty(eventId);

        return SqlSelectTo.theClass(Event.class)
                .sql(EventTable.SELECT_EVENT_BY_ID)
                .param(eventId)
                .applyTo(source)
                .stream().findFirst().orElseThrow();
    }

    @Override
    @SneakyThrows
    public EventDetail loadEventDetails(String eventId, String nickname) {
        var eventDetail = SqlSelectTo.theClass(EventDetail.class)
                .sql(EventTable.BASE_SELECT_DETAILS_BY_ID)
                .param(eventId)
                .applyTo(source)
                .stream().findFirst().orElseThrow();

        eventDetail.id = eventId;
        eventDetail.canFollow = eventDetail.current < eventDetail.limit;

        if (StringUtils.isNotNullOrEmpty(eventDetail.organizationId)) {
            eventDetail.organization = organizationRegister.loadOrgById(eventDetail.organizationId);
        }

        if (StringUtils.isNotNullOrEmpty(nickname) && !Objects.equals(nickname, "anonymousUser")) {
            var user = userRegister.loadUserByNickName(nickname);

            var id = SqlSelectTo.theClass(EventId.class)
                    .sql(UserTable.SELECT_EVENT_BY_USER_ID)
                    .params(List.of(user.id, eventId))
                    .applyTo(source)
                    .stream().findFirst().orElse(null);

            eventDetail.isFollowed = id != null;

            var userRating = SqlSelectTo.theClass(Rating.class)
                    .sql(USER_RATING)
                    .params(List.of(eventId, user.id))
                    .applyTo(source).stream().findFirst().orElse(null);

            eventDetail.userRating = userRating == null ? 1 : userRating.rating;
        }

        return eventDetail;
    }

    @Override
    public List<User> loadEventParticipants(String eventId) {
        Strings.requiresNotNullOrEmpty(eventId);

        return SqlSelectTo.theClass(User.class)
                .sql(UserTable.SELECT_USERS_BY_EVENT)
                .param(eventId)
                .applyTo(source);
    }

    @Override
    public List<Event> loadFavoriteEvents(String userId) {
        var eventIds = SqlSelectTo.theClass(EventId.class)
                .sql(UserTable.SELECT_FAVORITE_EVENT_IDS_BY_USER_ID)
                .param(userId)
                .applyTo(source);

        return eventByIds(eventIds);
    }

    @Override
    public List<Event> loadEventsByUser(String userId) {
        var eventIds = SqlSelectTo.theClass(EventId.class)
                .sql(UserTable.SELECT_EVENT_IDS_BY_USER_ID)
                .param(userId)
                .applyTo(source);

        return eventByIds(eventIds);
    }

    @Override
    public int setNewRating(String userId, String eventId, int rating) {
        try (var connection = source.getConnection()) {
            SqlUpsert.into("event_rating")
                    .key("user_id", userId)
                    .key("event_id", eventId)
                    .field("rating", rating)
                    .toUpdate()
                    .ifPresent(u -> u.applyTo(connection));

            return countRating(connection, eventId, rating);
        } catch (Exception e) {
            throw new RuntimeException("flg32@fj", e);
        }

    }

    @SneakyThrows
    private int countRating(Connection connection, String eventId, int rating) {
        try (var ps = connection.prepareStatement(COUNT_AVG_RATING)) {
            ps.setString(1, eventId);

            var rs = ps.executeQuery();

            return rs.next() ? rs.getInt(AVG_COLUMN_NAME) : rating;
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    private List<Event> eventByIds(List<EventId> eventIds) {
        var eventList = new ArrayList<Event>();

        for (var eventId : eventIds) {
            eventList.add(
                    SqlSelectTo.theClass(Event.class)
                            .sql(EventTable.SELECT_EVENT_BY_ID)
                            .param(eventId.id)
                            .applyTo(source)
                            .stream().findFirst().orElse(null)
            );
        }

        return eventList;
    }

}
