package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.security.Role;



public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByNome(String nome);
}
