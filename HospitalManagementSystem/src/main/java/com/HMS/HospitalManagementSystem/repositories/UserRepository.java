package com.HMS.HospitalManagementSystem.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HMS.HospitalManagementSystem.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Spring will use this to find the user during login
    Optional<User> findByEmail(String email);
    
}
