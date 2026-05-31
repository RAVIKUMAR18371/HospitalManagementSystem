// package com.HMS.HospitalManagementSystem.entities;

// import java.util.*;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.Table;
// import jakarta.validation.constraints.Email;

// @Entity
// @Table(name = "users")
// public class User implements UserDetails {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Integer id;
    
//     // Changed to lowercase 'e' for standard Java naming conventions
//     private String email;

//     // 1. Added Password Field
//     private String password;

//     @ManyToOne
//     @JoinColumn(name = "role_id", referencedColumnName="id", nullable=false)
//     private Role role;

//     /*This method returns a list of permissions (called Authorities) that Spring Security uses to decide what the user is allowed to do on your website.
// Think of it like a security badge. When a user logs in, Spring Security asks, "What labels are on this person's badge?" Your code creates a badge labeled ROLE_ADMIN (or whatever your role name is) and hands it to Spring.
// Because Spring Security is designed to handle users with many roles, it insists that you return a Collection (a list), even if your user only has one single role. Using List.of(authority) simply wraps your one specific role into that required list format so the security system can read it. */
//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities(){
//         // 2. Fixed Prefix: Spring Security absolutely requires "ROLE_" in all caps [1]
//         SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());
//         return List.of(authority);
//     }


//     // 3. Fixed Get Password: Must return the actual password field for login verification
//     @Override
//     public String getPassword(){
//         return this.password;
//     }

//     public void setPassword(String password){
//         this.password = password;
//     }

//     public void setRole(Role role){
//         this.role = role;
//     }

//     public Role getRole(){
//         return role;
//     }

//     // 4. Fixed GetUserName: We are using email to log in, so return the email here
//     @Override
//     public String getUsername(){
//         return this.email;
//     }

//     // NOW I AM SETTING THE STANDARD GETTER AND SETTER

//     public Integer getId(){
//         return id;
//     }

//     public void setId(Integer id){
//         this.id = id;
//     }

//     public void setEmail(String email){
//         this.email = email;
//     }

//     public String getEmail(){
//         return email;
//     }

//     // 5. REQUIRED BY SPRING SECURITY: These must return true, or the account is considered locked/disabled
//     @Override
//     public boolean isAccountNonExpired(){
//         return true;
//     }

//     @Override
//     public boolean isAccountNonLocked(){
//         return true;
//     }

//     @Override
//     public boolean isCredentialsNonExpired(){
//         return true;
//     }

//     @Override
//     public boolean isEnabled(){
//         return true;
//     }

  
// }

package com.HMS.HospitalManagementSystem.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// BUG FIX #7: Removed unused @Email import (jakarta.validation.constraints.Email was imported
// but never used on any field — causes IDE warnings and confuses readers).
// BUG FIX #8: Removed unused @OneToOne import that wasn't used in this class.

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

