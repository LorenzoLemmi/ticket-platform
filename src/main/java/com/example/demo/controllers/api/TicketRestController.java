package com.example.demo.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Ticket;
import com.example.demo.repositories.TicketRepository;



@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @GetMapping("/categoria/{id}")
    public List<Ticket> getTicketsByCategoria(@PathVariable Integer id) {
        return ticketRepository.findByCategoriaId(id);
    }

    @GetMapping("/stato/{stato}")
    public List<Ticket> getTicketsByStato(@PathVariable Ticket.StatoTicket stato) {
        return ticketRepository.findByStato(stato);
    }

}
