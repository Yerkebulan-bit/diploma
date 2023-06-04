package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.model.ticket.Ticket;
import kz.iitu.diploma_resource_server.model.ticket.TicketId;
import kz.iitu.diploma_resource_server.register.EventRegister;
import kz.iitu.diploma_resource_server.register.TicketRegister;
import kz.iitu.diploma_resource_server.util.Strings;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketRegisterImpl implements TicketRegister {

    private final DataSource source;

    private final EventRegister eventRegister;

    @Autowired
    public TicketRegisterImpl(DataSource source, @Lazy EventRegister eventRegister) {
        this.source = source;
        this.eventRegister = eventRegister;
    }

    @Override
    public void saveTicket(String userId, String eventId) {
        Strings.requiresNotNullOrEmpty(userId);
        Strings.requiresNotNullOrEmpty(eventId);

        SqlUpsert.into("ticket")
                .key("id", UUID.randomUUID().toString())
                .field("user_id", userId)
                .field("event_id", eventId)
                .toUpdate()
                .ifPresent(u -> u.applyTo(source));
    }

    @Override
    public List<Ticket> loadUserTickets(String userId) {
        Strings.requiresNotNullOrEmpty(userId);

        var ticketIds = SqlSelectTo.theClass(TicketId.class)
                .sql(
                        "SELECT id, event_id as eventId " +
                                "FROM ticket " +
                                "WHERE user_id = ?"
                )
                .param(userId)
                .applyTo(source);

        return ticketIds.stream()
                .map(ticketId -> createTicket(ticketId.eventId, ticketId.id))
                .collect(Collectors.toList());
    }

    private Ticket createTicket(String eventId, String ticketId) {
        return Ticket.of(eventRegister.loadEventById(eventId), ticketId);
    }

}
