package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.ticket.Ticket;

import java.util.List;

public interface TicketRegister {

    void saveTicket(String userId, String eventId);

    List<Ticket> loadUserTickets(String userId);

}
