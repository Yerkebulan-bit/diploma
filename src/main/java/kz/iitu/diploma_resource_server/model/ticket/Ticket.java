package kz.iitu.diploma_resource_server.model.ticket;

import kz.iitu.diploma_resource_server.model.event.Event;

public class Ticket {

    public String id;
    public String eventName;
    public String eventTime;

    public static Ticket of(Event event, String id) {
        var ticket = new Ticket();

        ticket.eventName = event.name;
        ticket.eventTime = event.startedAt;
        ticket.id = id;

        return ticket;
    }
}
