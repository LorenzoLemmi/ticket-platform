package com.example.demo.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entities.Operatore;



public class DatabaseUserDetails implements UserDetails {

    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;

    public DatabaseUserDetails(Operatore operatore) {
        this.username = operatore.getEmail();
        this.password = operatore.getPassword();
        this.authorities = new HashSet<>();

        for(Role role : operatore.getRuoli()) {
            SimpleGrantedAuthority sGA = new SimpleGrantedAuthority(role.getNome());
            this.authorities.add(sGA);
        }
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}