package com.HMS.HospitalManagementSystem.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HMS.HospitalManagementSystem.entities.Patient;
import com.HMS.HospitalManagementSystem.security.EncryptionService;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final EncryptionService encryptionService;

    public RegistrationController(EncryptionService encryptionService){
        this.encryptionService = encryptionService;
    }

    // Step1: Open to the public for creating an account requests
    public String registerPatient(@RequestBody Patient patient) throws Exception{
        // Encrypting the Sensitive Data before saving
        patient.setSecretId(encryptionService.encryptAES128(patient)); // It is needed for the document verification and it is used for that.
        // Hashing the record to prevent tempering and now 

        // Save to database with status "PENDING"
       // patientRepository.save(patient);

        return "Registration request submitted to admin for approval.";
    }

    // step2: Accessible Only by Admin for approving the registration requests
    @PostMapping("/approve/{patientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approvePatientRequest(@PathVariable Integer patientId){
        // Fetch the patient from the database, change status from PENDING to APPROVED
        // patient.setStatus("Approved");
        // patientRepository.save(patient);

        return "Patient request approved. User can now log in.";
    }

    
}
