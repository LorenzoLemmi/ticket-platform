package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name="ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Il titolo è obbligatorio")
    @Column(name="titolo", nullable=false)
    private String titolo;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Column(name="descrizione", nullable=false)
    private String descrizione;

    public enum StatoTicket {
        da_fare,
        in_corso,
        completato;
    }

    @NotNull(message = "Lo dichiarazione dello stato del ticket è obbligatoria")
    @Column(name="stato", nullable=false)
    @Enumerated(EnumType.STRING)
    private StatoTicket stato;

    @CreationTimestamp
    @Column(name="data_creazione", nullable=false)
    private LocalDateTime dataCreazione;

    @ManyToOne
    @JoinColumn(name="categoria_id", nullable=false)
    @JsonBackReference
    private Categoria categoria;

    @OneToMany(mappedBy="ticket")
    private List<Nota> noteList;
    
    @ManyToOne
    @JoinColumn(name="operatore_id")
    private Operatore operatore;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Operatore getOperatore() {
        return operatore;
    }

    public void setOperatore(Operatore operatore) {
        this.operatore = operatore;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public StatoTicket getStato() {
        return stato;
    }

    public void setStato(StatoTicket stato) {
        this.stato = stato;
    }

    public List<Nota> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Nota> noteList) {
        this.noteList = noteList;
    }
}
