package com.HMS.HospitalManagementSystem.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@EnableMethodSecurity
public class UserController {
    // Accessing by any authenticated user
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String getAuthenticatedUser(){
        return "Authenticated User Details";
    }

    // Accessible ONLY by Admin or Super Admin
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public String getAllUsers(){
        return "List of all hospital users";
    }
}
