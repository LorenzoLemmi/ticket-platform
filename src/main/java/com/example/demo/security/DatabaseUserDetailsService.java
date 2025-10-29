package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entities.Operatore;
import com.example.demo.repositories.OperatoreRepository;



public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private OperatoreRepository operatoreRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Operatore operatore = operatoreRepository.findByEmail(email);
        if (operatore == null) {
            throw new UsernameNotFoundException("Operatore con email '" + email + "' non trovato");
        }

        return new DatabaseUserDetails(operatore); 
    }

}
