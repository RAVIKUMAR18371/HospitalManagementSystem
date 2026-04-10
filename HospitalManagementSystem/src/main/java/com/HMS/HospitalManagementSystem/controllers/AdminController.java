package com.HMS.HospitalManagementSystem.controllers;

import com.HMS.HospitalManagementSystem.entities.Role;
import com.HMS.HospitalManagementSystem.entities.RoleEnum;
import com.HMS.HospitalManagementSystem.entities.User;
import com.HMS.HospitalManagementSystem.repositories.RoleRepository;
import com.HMS.HospitalManagementSystem.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/management")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // SUPER_ADMIN → New Admin banata hai
    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String createAdmin(@RequestBody User newAdminData) {
        if (userRepository.findByEmail(newAdminData.getEmail()).isPresent()) {
            return "Error: Email already exists!";
        }
        User admin = new User();
        admin.setEmail(newAdminData.getEmail());
        admin.setPassword(passwordEncoder.encode(newAdminData.getPassword()));

        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN)
                .orElseThrow(() -> new RuntimeException("ADMIN Role not found"));
        admin.setRole(adminRole);
        userRepository.save(admin);
        return "New Admin created successfully!";
    }

    // ADMIN ya SUPER_ADMIN → New Doctor banata hai
    @PostMapping("/create-doctor")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public String createDoctor(@RequestBody User newDoctorData) {
        if (userRepository.findByEmail(newDoctorData.getEmail()).isPresent()) {
            return "Error: Email already exists!";
        }
        User doctor = new User();
        doctor.setEmail(newDoctorData.getEmail());
        doctor.setPassword(passwordEncoder.encode(newDoctorData.getPassword()));

        Role doctorRole = roleRepository.findByName(RoleEnum.DOCTOR)
                .orElseThrow(() -> new RuntimeException("DOCTOR Role not found"));
        doctor.setRole(doctorRole);
        userRepository.save(doctor);
        return "New Doctor created successfully!";
    }
}