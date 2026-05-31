// package com.HMS.HospitalManagementSystem.controllers;

// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("api/users")
// @EnableMethodSecurity
// public class UserController {
//     // Accessing by any authenticated user
//     @GetMapping("/me")
//     @PreAuthorize("isAuthenticated()")
//     public String getAuthenticatedUser(){
//         return "Authenticated User Details";
//     }

//     // Accessible ONLY by Admin or Super Admin
//     @GetMapping
//     @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
//     public String getAllUsers(){
//         return "List of all hospital users";
//     }
// }


package com.HMS.HospitalManagementSystem.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// BUG FIX #26: @EnableMethodSecurity was placed on this controller class.
// This annotation belongs ONLY on a @Configuration class (which SecurityConfig
// already has it). Placing it on a @RestController is wrong — it creates a
// duplicate security proxy and can cause unexpected Spring context issues.
// Removed from here; SecurityConfig already enables method security globally.

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String getAuthenticatedUser() {
        return "Authenticated User — you are logged in.";
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public String getAllUsers() {
        return "List of all hospital users — accessible by Admin and SuperAdmin only.";
    }
}
