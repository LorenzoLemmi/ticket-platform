package com.example.demo.security;

import java.util.List;

import com.example.demo.entities.Operatore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome; 

    @ManyToMany(mappedBy = "ruoli")
    private List<Operatore> operatori;

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

    public List<Operatore> getOperatori() {
        return operatori;
    }

    public void setOperatori(List<Operatore> operatori) {
        this.operatori = operatori;
    }

}

