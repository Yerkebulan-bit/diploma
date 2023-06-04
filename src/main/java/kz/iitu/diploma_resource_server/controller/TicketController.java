package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.ticket.Ticket;
import kz.iitu.diploma_resource_server.register.TicketRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketRegister ticketRegister;

    @Autowired
    public TicketController(TicketRegister ticketRegister) {
        this.ticketRegister = ticketRegister;
    }

    @GetMapping("/load-user-tickets")
    public List<Ticket> loadUserTickets(@RequestParam("userId") String userId) {
        return ticketRegister.loadUserTickets(userId);
    }
}
