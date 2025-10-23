package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Operatore;



public interface OperatoreRepository extends JpaRepository<Operatore, Integer> {

    Optional<Operatore> findFirstByStato(Operatore.StatoOperatore stato);
    
    Operatore findByEmail(String email);
}