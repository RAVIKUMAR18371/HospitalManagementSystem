// package com.HMS.HospitalManagementSystem.controllers;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.HMS.HospitalManagementSystem.entities.Patient;
// import com.HMS.HospitalManagementSystem.entities.Role;
// import com.HMS.HospitalManagementSystem.entities.RoleEnum;
// import com.HMS.HospitalManagementSystem.entities.User;
// import com.HMS.HospitalManagementSystem.repositories.PatientRepository;
// import com.HMS.HospitalManagementSystem.repositories.RoleRepository;
// import com.HMS.HospitalManagementSystem.repositories.UserRepository;
// import com.HMS.HospitalManagementSystem.security.EncryptionService;

// @RestController
// @RequestMapping("/api/register")
// public class RegistrationController {

//   private final PatientRepository patientRepository;
//   private final UserRepository userRepository;
//   private final RoleRepository roleRepository;
//   private final EncryptionService encryptionService;
//   private final PasswordEncoder passwordEncoder;

//   @Autowired
//   public RegistrationController(PatientRepository patientRepository, UserRepository userRepository,
//                                 RoleRepository roleRepository, EncryptionService encryptionService,
//                                 PasswordEncoder passwordEncoder){

//         this.patientRepository = patientRepository;
//         this.userRepository = userRepository;
//         this.roleRepository = roleRepository;
//         this.encryptionService = encryptionService;
//         this.passwordEncoder = passwordEncoder;

//      }

//      @PostMapping("/patient")
// public String registerPatient(@RequestBody Patient patient) throws Exception {

//     // CHECK 1 — Duplicate email check
//     if(userRepository.findByEmail(patient.getEmail()).isPresent()) {
//         return "Error: Email already registered!";
//     }

//     // CHECK 2 — Encrypt secretId
//     patient.setSecretId(encryptionService.encryptAES128(patient.getSecretId()));
//     patient.setStatus("PENDING");

//     // CHECK 3 — Pehle User banao
//     User newUser = new User();
//     newUser.setEmail(patient.getEmail());
//     newUser.setPassword(passwordEncoder.encode(patient.getPassword()));

//     Role userRole = roleRepository.findByName(RoleEnum.USER)
//                     .orElseThrow(() -> new RuntimeException("Role not found"));
//     newUser.setRole(userRole);
//     userRepository.save(newUser); // ← Pehle user save

//     //CHECK 4 — Phir Patient banao with user link
//     patient.setUser(newUser);
//     patientRepository.save(patient); // ← Phir patient save

//     return "Registration successful. Please wait for Admin Approval to log in";
// }
  

    
// }

package com.HMS.HospitalManagementSystem.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HMS.HospitalManagementSystem.entities.Patient;
import com.HMS.HospitalManagementSystem.entities.Role;
import com.HMS.HospitalManagementSystem.entities.RoleEnum;
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

    // BUG FIX #18: Removed @Autowired from constructor. With a single constructor,
    // Spring auto-injects without @Autowired — adding it is redundant and can cause
    // confusion. More importantly, it was mixed with field injection style in original.
    public RegistrationController(PatientRepository patientRepository, UserRepository userRepository,
                                   RoleRepository roleRepository, EncryptionService encryptionService,
                                   PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encryptionService = encryptionService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/patient")
    public String registerPatient(@RequestBody Patient patient) throws Exception {

        // Check 1: Duplicate email in User table
        if (userRepository.findByEmail(patient.getEmail()).isPresent()) {
            return "Error: Email already registered!";
        }

        // Check 2: Encrypt the secretId before saving
        if (patient.getSecretId() != null && !patient.getSecretId().isEmpty()) {
            patient.setSecretId(encryptionService.encryptAES128(patient.getSecretId()));
        }

        patient.setStatus("PENDING");

        // Check 3: Create the User account first (needed for FK in Patient)
        User newUser = new User();
        newUser.setEmail(patient.getEmail());

        // BUG FIX #19: The patient's password field holds a plain-text password from
        // the registration form. We must BCrypt-hash it before storing in the User table.
        // The original code already did this — preserved here.
        newUser.setPassword(passwordEncoder.encode(patient.getPassword()));

        Role userRole = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("USER Role not found"));
        newUser.setRole(userRole);
        userRepository.save(newUser);

        // Check 4: Link the Patient to the newly created User
        patient.setUser(newUser);

        // BUG FIX #20: Patient entity has a "password" field that stores the
        // original plain-text password from the form body. This must NOT be
        // stored in plain text in the DB. Clear it after using it to create User.
        patient.setPassword(null);

        patientRepository.save(patient);

        return "Registration successful. Please wait for Admin Approval to login.";
    }
}
