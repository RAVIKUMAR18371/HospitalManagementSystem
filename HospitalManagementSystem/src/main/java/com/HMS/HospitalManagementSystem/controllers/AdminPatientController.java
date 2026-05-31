package com.HMS.HospitalManagementSystem.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HMS.HospitalManagementSystem.entities.Patient;
import com.HMS.HospitalManagementSystem.repositories.PatientRepository;
import com.HMS.HospitalManagementSystem.security.EncryptionService;

@RestController
@RequestMapping("/api/admin/patients")
public class AdminPatientController {

    private final PatientRepository patientRepository;
    private final EncryptionService encryptionService;

    public AdminPatientController(PatientRepository patientRepository, EncryptionService encryptionService) {
        this.patientRepository = patientRepository;
        this.encryptionService = encryptionService;
    }

    @PutMapping("/update/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    // BUG FIX #23: Original had @PreAuthorize("hasRole('ADMIN')") — this means
    // SUPER_ADMIN cannot update patients, which is logically wrong. A super admin
    // should have all admin powers. Fixed to hasAnyRole('ADMIN', 'SUPER_ADMIN').
    public String updatePatient(@PathVariable Integer patientId, @RequestBody Patient updatedData) throws Exception {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        // Update allowed fields
        if (updatedData.getAge() != null) {
            existingPatient.setAge(updatedData.getAge());
        }
        if (updatedData.getAddress() != null) {
            existingPatient.setAddress(updatedData.getAddress());
        }

        // Re-encrypt secretId if provided
        if (updatedData.getSecretId() != null && !updatedData.getSecretId().isEmpty()) {
            existingPatient.setSecretId(encryptionService.encryptAES128(updatedData.getSecretId()));
        }

        patientRepository.save(existingPatient);
        return "Patient updated successfully.";
    }

    @DeleteMapping("/delete/{patientId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deletePatient(@PathVariable Integer patientId) {
        if (!patientRepository.existsById(patientId)) {
            return "Error: Patient does not exist.";
        }
        patientRepository.deleteById(patientId);
        return "Patient successfully deleted from the system.";
    }
}