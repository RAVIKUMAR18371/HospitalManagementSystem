package com.HMS.HospitalManagementSystem.controllers;

import com.HMS.HospitalManagementSystem.entities.Patient;
import com.HMS.HospitalManagementSystem.entities.Role;
import com.HMS.HospitalManagementSystem.entities.RoleEnum;
import com.HMS.HospitalManagementSystem.entities.User;
import com.HMS.HospitalManagementSystem.repositories.PatientRepository;
import com.HMS.HospitalManagementSystem.repositories.RoleRepository;
import com.HMS.HospitalManagementSystem.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    // BUG FIX #21: AdminController needed PatientRepository to support listing
    // pending patients — without it, the admin dashboard frontend has no way
    // to get a list of patients who need approval.
    private final PatientRepository patientRepository;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
    }

    // SUPER_ADMIN → Creates a new Admin
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

    // ADMIN or SUPER_ADMIN → Creates a new Doctor
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

    // BUG FIX #21 continued: Added endpoint to list all PENDING patients
    // so Admin can see who is waiting for approval.
    @GetMapping("/pending-patients")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<Patient> getPendingPatients() {
        return patientRepository.findByStatus("PENDING");
    }

    // BUG FIX #22: Added approve-patient endpoint. Without this, there is
    // NO way to approve a patient — they would be stuck in PENDING forever
    // and could never log in (the login works but the doctor/admin would
    // never know who is approved). Status change to APPROVED completes the flow.
    @PutMapping("/approve-patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public String approvePatient(@PathVariable Integer patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setStatus("APPROVED");
        patientRepository.save(patient);
        return "Patient ID " + patientId + " has been approved successfully.";
    }
}
