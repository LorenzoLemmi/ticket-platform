package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Operatore;
import com.example.demo.entities.Ticket;


public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    public List<Ticket> findByTitoloContainingIgnoreCase(String titolo);

    public Optional<Ticket> findByTitolo(String titolo);

    List<Ticket> findByOperatore(Operatore operatore);

    List<Ticket> findByOperatoreId(Integer operatoreId);

    boolean existsByOperatoreIdAndStatoIn(Integer operatoreId, List<Ticket.StatoTicket> stati);

    List<Ticket> findByCategoriaId(Integer categoriaId);

    List<Ticket> findByStato(Ticket.StatoTicket stato);
}
