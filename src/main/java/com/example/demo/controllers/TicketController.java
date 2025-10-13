package com.example.demo.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entities.Nota;
import com.example.demo.entities.Ticket;
import com.example.demo.repositories.NotaRepository;
import com.example.demo.repositories.TicketRepository;



@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private NotaRepository notaRepository;

    @GetMapping
    public String showIndex(Model model, @RequestParam(name = "keyword", required=false) String keyword) {

        List<Ticket> ticketList;
        if (keyword == null || keyword.isBlank() ) {
            ticketList = ticketRepository.findAll();
        } else {
            ticketList = ticketRepository.findByTitoloContainingIgnoreCase(keyword);
        }

        model.addAttribute("ticketList", ticketList);
        return "index";

    }

    @GetMapping("/show/{id}")
    public String showTicketDetail(@PathVariable("id") Integer id, Model model) {

        Optional<Ticket> optTicket = ticketRepository.findById(id);
        if (optTicket.isPresent()) {
            model.addAttribute("ticket", optTicket.get());
            model.addAttribute("empty", false);
            return "ticketDetail";
        } else {
            model.addAttribute("empty", true);
        }
        return "ticketDetail";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("list", ticketRepository.findAll());
        model.addAttribute("ticket", new Ticket());
        return "createForm";
    }

    @PostMapping("/create")
    public String createSubmit(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model) {

        Optional<Ticket> optTicket = ticketRepository.findByTitolo(formTicket.getTitolo());
        if (optTicket.isPresent()) {
            bindingResult.addError(new ObjectError("nome", "Nome già presente"));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("list", ticketRepository.findAll());
            return "createForm";
        }
        ticketRepository.save(formTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket creato con successo");
        return "redirect:/tickets";

    }

    @GetMapping("/update/{id}")
    public String updateTicket(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("list", ticketRepository.findAll());
        model.addAttribute("ticket", ticketRepository.findById(id).get());
        return "updateForm";
    }

    @PostMapping("/update/{id}")
    public String updateSubmit(@PathVariable("id") Integer id, @Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult,
                                Model model) {
        Ticket oldTicket = ticketRepository.findById(formTicket.getId()).get();
        if (!oldTicket.getTitolo().equalsIgnoreCase(formTicket.getTitolo())) {
            bindingResult.addError(new ObjectError("titolo", "Non è possibile modificare il titolo del ticket"));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("list", ticketRepository.findAll());
            return "updateForm";
        }
        ticketRepository.save(formTicket);
        return "redirect:/tickets";

    }

    @PostMapping("/delete/{id}")
    public String deleteTicket(@PathVariable("id") Integer id) {
        Ticket ticket = ticketRepository.findById(id).get();
        for (Nota notaToDelete : ticket.getNoteList()) {
            notaRepository.delete(notaToDelete);
        }
        ticketRepository.deleteById(id);
        return "redirect:/tickets";
    }
}
