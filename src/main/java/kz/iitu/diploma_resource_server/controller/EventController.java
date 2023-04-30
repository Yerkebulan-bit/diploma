package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.SearchFilter;
import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.event.Day;
import kz.iitu.diploma_resource_server.model.event.Event;
import kz.iitu.diploma_resource_server.model.event.EventDetail;
import kz.iitu.diploma_resource_server.model.event.EventToSave;
import kz.iitu.diploma_resource_server.register.EventRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    public final EventRegister eventRegister;

    @Autowired
    public EventController(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }

    @PostMapping("/save-event")
    public String saveEvent(@RequestBody EventToSave event) {
        return eventRegister.saveEvent(event);
    }

    @GetMapping("/load-main-event")
    public List<Event> loadMainEvent() {
        return eventRegister.loadMainEvents();
    }

    @GetMapping("/load-week-events")
    public List<Event> loadWeekEvent() {
        return eventRegister.loadWeekEvents();
    }

    @PostMapping("/load-events")
    public List<Event> loadEvents(SearchFilter filter, Day day) {
        return eventRegister.loadEvents(filter, day);
    }

    @GetMapping("/load-events-by-org")
    public List<Event> loadEventsByOrd(@RequestParam("orgId") String orgId) {
        return eventRegister.loadOrgEvents(orgId);
    }

    @GetMapping("/load-event-detail/{id}")
    public EventDetail loadEventDetail(@PathVariable("id") String eventId) {
        return eventRegister.loadEventDetails(eventId);
    }

    @GetMapping("/load-event-users")
    public List<User> loadEventUsers(@RequestParam("eventId") String eventId) {
        return eventRegister.loadEventParticipants(eventId);
    }

}