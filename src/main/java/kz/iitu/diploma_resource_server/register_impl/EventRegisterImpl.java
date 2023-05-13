package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.model.SearchFilter;
import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.event.Day;
import kz.iitu.diploma_resource_server.model.event.Event;
import kz.iitu.diploma_resource_server.model.event.EventDetail;
import kz.iitu.diploma_resource_server.model.event.EventToSave;
import kz.iitu.diploma_resource_server.register.EventRegister;
import kz.iitu.diploma_resource_server.register.OrganizationRegister;
import kz.iitu.diploma_resource_server.sql.EventTable;
import kz.iitu.diploma_resource_server.sql.SqlBuilder;
import kz.iitu.diploma_resource_server.sql.UserTable;
import kz.iitu.diploma_resource_server.util.StringUtils;
import kz.iitu.diploma_resource_server.util.Strings;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Component
public class EventRegisterImpl implements EventRegister {

    private final DataSource source;
    private final OrganizationRegister organizationRegister;

    @Autowired
    public EventRegisterImpl(DataSource source, OrganizationRegister organizationRegister) {
        this.source = source;
        this.organizationRegister = organizationRegister;
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

        return SqlSelectTo.theClass(Event.class)
                .sql(sql)
                .applyTo(source);
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
    public EventDetail loadEventDetails(String eventId) {
        var eventDetail = SqlSelectTo.theClass(EventDetail.class)
                .sql(EventTable.BASE_SELECT_DETAILS_BY_ID)
                .param(eventId)
                .applyTo(source)
                .stream().findFirst().orElseThrow();

        eventDetail.id = eventId;

        if (StringUtils.isNotNullOrEmpty(eventDetail.organizationId)) {
            eventDetail.organization = organizationRegister.loadOrgById(eventDetail.organizationId);
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
        return null;
    }

}
