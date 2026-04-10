package com.HMS.HospitalManagementSystem.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.HMS.HospitalManagementSystem.entities.User;
import com.HMS.HospitalManagementSystem.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        return user.get(); // your User already implements UserDetails ✅
    }
}