package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="operatore")
public class Operatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="nome", nullable=false)
    private String nome;

    @Column(name="email", nullable=false)
    private String email;
    
    @Column(name="password", nullable=false)
    private String password;

    @Column(name="ruolo", nullable=false)
    private String ruolo;

    public enum StatoOperatore {
        disponibile,
        non_disponibile,
        non_attivo
    }

    @Column(name="stato", length = 30)
    @Enumerated(EnumType.STRING)
    private StatoOperatore stato;

    @OneToMany(mappedBy="operatore")
    private List<Ticket> ticketsList;

    @OneToMany(mappedBy="operatore")
    private List<Nota> noteList;

    public List<Ticket> getTicketsList() {
        return ticketsList;
    }

    public void setTicketsList(List<Ticket> ticketsList) {
        this.ticketsList = ticketsList;
    }

    public List<Nota> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Nota> noteList) {
        this.noteList = noteList;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public StatoOperatore getStato() {
        return stato;
    }

    public void setStato(StatoOperatore stato) {
        this.stato = stato;
    }

}
