package com.example.demo.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entities.Nota;
import com.example.demo.entities.Operatore;
import com.example.demo.entities.Ticket;
import com.example.demo.repositories.NotaRepository;
import com.example.demo.repositories.OperatoreRepository;
import com.example.demo.repositories.TicketRepository;

import jakarta.validation.Valid;


@Controller
public class NotaController {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private OperatoreRepository operatoreRepository;
    @Autowired
    private NotaRepository notaRepository;


    @GetMapping("/note/crea/{ticketId}")
    public String creaNotaForm(@PathVariable("ticketId") Integer ticketId, Model model) {
        Optional<Ticket> optTicket = ticketRepository.findById(ticketId);

        if (optTicket.isPresent()) {
            Nota nota = new Nota();
            nota.setTicket(optTicket.get());
            model.addAttribute("nota", nota);
            model.addAttribute("empty", false);
        } else {
            model.addAttribute("empty", true);
        }

        return "notaForm";
    }


    @PostMapping("/note/crea/{ticketId}")
    public String creaNotaSubmit(@PathVariable("ticketId") Integer ticketId,
                                @ModelAttribute ("nota") @Valid Nota nota, BindingResult bindingResult,
                                Principal principal, RedirectAttributes redirectAttributes, Model model) {

        Optional<Ticket> optTicket = ticketRepository.findById(ticketId);
        if (optTicket.isEmpty()) {
            return "redirect:/tickets";
        }

        
        if (nota.getTesto() == null || nota.getTesto().trim().isEmpty()) {
            bindingResult.addError(new ObjectError("testo", "Il testo della nota non può essere vuoto"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("nota", nota);
            model.addAttribute("ticket", optTicket.get());
            return "notaForm";
        }

        nota.setTicket(optTicket.get());
        nota.setDataCreazione(LocalDateTime.now());

        // Recupera l’operatore loggato
        if (principal != null) {
            String email = principal.getName();
            Operatore operatore = operatoreRepository.findByEmail(email);
            nota.setOperatore(operatore);
        }

        notaRepository.save(nota);

        redirectAttributes.addFlashAttribute("successMessage", "Nota aggiunta con successo!");
        return "redirect:/tickets/show/" + ticketId;
    }


}
