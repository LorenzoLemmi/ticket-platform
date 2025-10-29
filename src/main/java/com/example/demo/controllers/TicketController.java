package com.example.demo.controllers;

import java.time.LocalDateTime;
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

import com.example.demo.entities.Categoria;
import com.example.demo.entities.Nota;
import com.example.demo.entities.Operatore;
import com.example.demo.entities.Ticket;
import com.example.demo.repositories.CategoriaRepository;
import com.example.demo.repositories.NotaRepository;
import com.example.demo.repositories.OperatoreRepository;
import com.example.demo.repositories.TicketRepository;

import jakarta.validation.Valid;



@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private NotaRepository notaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private OperatoreRepository operatoreRepository;


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
        model.addAttribute("categorieList", categoriaRepository.findAll());
        model.addAttribute("ticketList", ticketRepository.findAll());
        model.addAttribute("ticket", new Ticket());
        return "createForm";
    }

    @PostMapping("/create")
    public String createSubmit(@Valid @ModelAttribute("ticket") Ticket formTicket,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model,
                            @RequestParam("categoriaId") Integer categoriaId) {
    
        // --- LOG TEMPORANEO ---
        System.out.println("DEBUG - createSubmit chiamato");
        System.out.println("Titolo: " + formTicket.getTitolo());
        System.out.println("Descrizione: " + formTicket.getDescrizione());
        System.out.println("Stato: " + formTicket.getStato());
        System.out.println("CategoriaId: " + categoriaId);

        Optional<Ticket> optTicket = ticketRepository.findByTitolo(formTicket.getTitolo());
        if (optTicket.isPresent()) {
            System.out.println("DEBUG: Titolo già presente");
            bindingResult.addError(new ObjectError("titolo", "Titolo già presente"));
        }

        if (bindingResult.hasErrors()) {
            System.out.println("DEBUG: BindingResult ha errori - dettagli:");
            bindingResult.getAllErrors().forEach(e -> {
            System.out.println(" - " + e.getObjectName() + ": " + e.getDefaultMessage());
            });
            model.addAttribute("categorieList", categoriaRepository.findAll());
            model.addAttribute("ticketList", ticketRepository.findAll());
            return "createForm";
        }

        Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoriaId);
        if (optionalCategoria.isPresent()) {
            formTicket.setCategoria(optionalCategoria.get());
        } else {
            System.out.println("DEBUG: Categoria non valida");
            bindingResult.addError(new ObjectError("categoria", "Categoria non valida"));
            model.addAttribute("categorieList", categoriaRepository.findAll());
            model.addAttribute("ticketList", ticketRepository.findAll());
            return "createForm";
        }

        // Assegna un operatore
        Optional<Operatore> optionalOperatore = operatoreRepository.findFirstByStato(Operatore.StatoOperatore.disponibile);
        if (optionalOperatore.isPresent()) {
            Operatore operatore = optionalOperatore.get();
            formTicket.setOperatore(operatore);
            operatoreRepository.save(operatore);
        } else {
            System.out.println("DEBUG: Assegnazione operatore");
            bindingResult.addError(new ObjectError("operatore", "Nessun operatore disponibile"));
            model.addAttribute("categorieList", categoriaRepository.findAll());
            model.addAttribute("ticketList", ticketRepository.findAll());
            return "createForm";
        }

        System.out.println("DEBUG: Salvataggio ticket");
        formTicket.setDataCreazione(LocalDateTime.now());

        ticketRepository.save(formTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket creato con successo");
        return "redirect:/tickets";
    }


    @GetMapping("/update/{id}")
    public String updateTicket(@PathVariable("id") Integer id, Model model) {
        System.out.println("DEBUG - GetMapping");
        model.addAttribute("categorieList", categoriaRepository.findAll());
        model.addAttribute("ticketList", ticketRepository.findAll());
        model.addAttribute("ticket", ticketRepository.findById(id).get());
        return "updateForm";
    }

    @PostMapping("/update/{id}")
    public String updateSubmit(@PathVariable("id") Integer id, @Valid @ModelAttribute("ticket") Ticket formTicket, 
                                @RequestParam("categoriaId") Integer categoriaId, BindingResult bindingResult,
                                Model model) {
        Ticket oldTicket = ticketRepository.findById(formTicket.getId()).get();
        //if (!oldTicket.getTitolo().equalsIgnoreCase(formTicket.getTitolo())) {
        //    System.out.println("DEBUG - Titolo uguale");
        //    bindingResult.addError(new ObjectError("titolo", "Non è possibile modificare il titolo del ticket"));
        //}

        Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoriaId);
        if(optionalCategoria.isPresent()) {
            System.out.println("Categoria presente");
            oldTicket.setCategoria(optionalCategoria.get());
        } else {
            System.out.println("DEBUG - Categoria non presente");
            bindingResult.addError(new ObjectError("categoria", "Categoria non valida"));
        }
        
        oldTicket.setTitolo(formTicket.getTitolo());
        oldTicket.setDescrizione(formTicket.getDescrizione());
        oldTicket.setStato(formTicket.getStato());

        if (bindingResult.hasErrors()) {
            model.addAttribute("categorieList", categoriaRepository.findAll());
            model.addAttribute("ticketList", ticketRepository.findAll());
            return "updateForm";
        }
        ticketRepository.save(oldTicket);
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

    @PostMapping("/updateStatus/{id}")
    public String updateStatus(@PathVariable("id") Integer id,
                                @RequestParam("stato") Ticket.StatoTicket nuovoStato) {

        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket != null) {
            ticket.setStato(nuovoStato);
            ticketRepository.save(ticket);
        }

        return "redirect:/tickets/show/" + id;
    }

}
