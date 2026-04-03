package com.HMS.HospitalManagementSystem.entities;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    
    /*This method returns a list of permissions (called Authorities) that Spring Security uses to decide what the user is allowed to do on your website.
Think of it like a security badge. When a user logs in, Spring Security asks, "What labels are on this person's badge?" Your code creates a badge labeled ROLE_ADMIN (or whatever your role name is) and hands it to Spring.
Because Spring Security is designed to handle users with many roles, it insists that you return a Collection (a list), even if your user only has one single role. Using List.of(authority) simply wraps your one specific role into that required list format so the security system can read it. */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("Role_" + role.getName().toString());
        return List.of(authority);
    }

    // Add other fields like username, password, and their getter/setter
    @Override
    public String getPassword(){
        return null;
    }

    @Override
    public String getUsername(){
        return null;
    }
}
