package com.HMS.HospitalManagementSystem.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    // This is where Postman will send the login request
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String Password){
        // Spring Security verifies the Bcrypt hashed password against the database
         Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, Password));

        // If successful, save the user in the secure context
        SecurityContextHolder.getContext().setAuthentication(authentication); // this is basically used to mark that the user is authenticated user.

        return "Login successful! I can now access protected Areas";
    }
    
}
