package com.example.demo.controllers;

import com.example.demo.entities.Operatore;
import com.example.demo.entities.Ticket;
import com.example.demo.repositories.OperatoreRepository;
import com.example.demo.repositories.TicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/operatori")
public class OperatoreController {

    @Autowired
    private OperatoreRepository operatoreRepository;

    @Autowired
    private TicketRepository ticketRepository;

    // Mostra la lista di tutti gli operatori
    @GetMapping
    public String showOperatori(Model model) {
        model.addAttribute("operatoriList", operatoreRepository.findAll());
        return "operatoriList";
    }

    // Mostra il dettaglio di un singolo operatore
    @GetMapping("/{id}")
    public String showOperatoreDetail(@PathVariable Integer id, Model model) {
        Operatore operatore = operatoreRepository.findById(id).orElse(null);
        if (operatore == null) return "redirect:/operatori";

        List<Ticket> ticketList = ticketRepository.findByOperatore(operatore);

        model.addAttribute("operatore", operatore);
        model.addAttribute("ticketList", ticketList);
        model.addAttribute("statiOperatore", Operatore.StatoOperatore.values());
        return "operatoreDetail";
    }

    // Aggiorna lo stato personale dell'operatore
    @PostMapping("/{id}/update")
    public String updateOperatore(@PathVariable Integer id, @RequestParam String nome, @RequestParam String email,
                                    @RequestParam("statoPersonale") Operatore.StatoOperatore nuovoStato, 
                                    RedirectAttributes redirectAttributes) {

        Operatore operatore = operatoreRepository.findById(id).orElse(null);
        if (operatore == null) return "redirect:/operatori";

        boolean aggiornato = true;

        // Se l'operatore prova a impostarsi come NON_ATTIVO, controlliamo i ticket assegnati
        if (nuovoStato == Operatore.StatoOperatore.non_attivo) {
            boolean haTicketAttivi = ticketRepository.existsByOperatoreIdAndStatoIn(
                    id,
                    Arrays.asList(Ticket.StatoTicket.da_fare, Ticket.StatoTicket.in_corso)
            );

            if (haTicketAttivi) {
                nuovoStato = operatore.getStato();
                aggiornato = false;
                redirectAttributes.addFlashAttribute("errorMessage",
                    "Impossibile impostare 'non attivo' con ticket ancora da fare o in corso.");
            }
        }

        operatore.setNome(nome);
        operatore.setEmail(email);
        operatore.setStato(nuovoStato);
        System.out.println("Nuovo stato ricevuto: " + nuovoStato);
        operatoreRepository.save(operatore);

        if (aggiornato) {
            redirectAttributes.addFlashAttribute("successMessage", "Dati aggiornati con successo!");
        }
        return "redirect:/operatori/" + id;
    }
}
