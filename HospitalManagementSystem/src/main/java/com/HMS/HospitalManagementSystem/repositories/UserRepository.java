// package com.HMS.HospitalManagementSystem.repositories;

// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.HMS.HospitalManagementSystem.entities.User;

// public interface UserRepository extends JpaRepository<User, Integer> {
//     // Spring will use this to find the user during login
//     Optional<User> findByEmail(String email);
    
// }


package com.HMS.HospitalManagementSystem.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.HMS.HospitalManagementSystem.entities.User;

// BUG FIX #12: Added missing @Repository annotation for clarity and
// to ensure Spring picks it up reliably as a bean.
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}

