package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.SearchFilter;
import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.event.Day;
import kz.iitu.diploma_resource_server.model.event.Event;
import kz.iitu.diploma_resource_server.model.event.EventDetail;
import kz.iitu.diploma_resource_server.model.event.EventToSave;

import java.util.List;

public interface EventRegister {

    String saveEvent(EventToSave event);

    List<Event> loadMainEvents();

    List<Event> loadWeekEvents();

    List<Event> loadEvents(SearchFilter filter, Day day);

    List<Event> loadOrgEvents(String orgId);

    Event loadEventById(String eventId);

    EventDetail loadEventDetails(String eventId, String nickname);

    List<User> loadEventParticipants(String eventId);

    List<Event> loadFavoriteEvents(String userId);

    List<Event> loadEventsByUser(String userId);

}
