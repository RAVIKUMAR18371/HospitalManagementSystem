package com.HMS.HospitalManagementSystem.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HMS.HospitalManagementSystem.entities.Patient;
import com.HMS.HospitalManagementSystem.entities.User;
import com.HMS.HospitalManagementSystem.repositories.PatientRepository;
import com.HMS.HospitalManagementSystem.repositories.RoleRepository;
import com.HMS.HospitalManagementSystem.repositories.UserRepository;
import com.HMS.HospitalManagementSystem.security.EncryptionService;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

  private final PatientRepository patientRepository;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final EncryptionService encryptionService;
  private final PasswordEncoder passwordEncoder;

  public RegistrationController(PatientRepository patientRepository, UserRepository userRepository,
                                RoleRepository roleRepository, EncryptionService encryptionService,
                                PasswordEncoder passwordEncoder){

        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encryptionService = encryptionService;
        this.passwordEncoder = passwordEncoder;

     }

     @PostMapping("/patient")
     public String registerPatient(@RequestBody Patient patient) throws Exception{
        // 1. Encrypt medical secret ID using AES-128
        patient.setSecretId(encryptionService.encryptAES128(patient.getSecretId()));
        patient.setStatus("PENDING");
        patientRepository.save(patient);

        // 2. Create the Spring Security User for Login
        User newUser = new User();
        newUser.setEmail(patient.getEmail());

        // Hash the Password using Bcrypt!
        newUser.setPassword(passwordEncoder.encode(patient.getPassword()));


        // 3. Assign the USER role
        Role userRole = roleRepository.findByName(RoleEnum.USER)
                                        .orElseThrow(() -> new RuntimeException("Role not found"));

        newUser.setRole(userRole);

        return "Registration successful. Please wait for Admin Approvaql to log in";
        
     }
  

    
}
