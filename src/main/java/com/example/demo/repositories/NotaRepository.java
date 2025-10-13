package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Nota;



public interface NotaRepository extends JpaRepository<Nota, Integer> {

}
