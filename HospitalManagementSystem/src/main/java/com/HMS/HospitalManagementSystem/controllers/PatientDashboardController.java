package com.HMS.HospitalManagementSystem.controllers;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.HMS.HospitalManagementSystem.entities.MedicalRecord;
import com.HMS.HospitalManagementSystem.repositories.MedicalRecordRepository;
import com.HMS.HospitalManagementSystem.security.EncryptionService;

@RestController
@RequestMapping("/api/patient-dashboard")
public class PatientDashboardController {

    private final MedicalRecordRepository recordRepository;
    private final EncryptionService encryptionService;

    // Injectting the repository via constructor
    public PatientDashboardController(MedicalRecordRepository recordRepository, EncryptionService encryptionService){
        this.recordRepository = recordRepository;
        this.encryptionService = encryptionService;
    }

    // Step 1: Display the Approved List of Documents
    @GetMapping("/{patientId}/approved-list")
    @PreAuthorize("hasRole('USER')") // Only accessible by patients
    public List<MedicalRecord> getApprovedList(@PathVariable Integer patientId) {
        // In a real application, you would query the database:
        // List<MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId);
        // The table must return data such as Patient ID, Doctor ID, Department, Description, File Key, and an Action button [1].
        return recordRepository.findByPatientId(patientId);
    }

    // Step 2: Verify the File Key and Decrypt the Document
    @PostMapping("/download")
    @PreAuthorize("hasRole('USER')")
    public String downloadAndDecryptDocument(
            @RequestParam Integer documentId, 
            @RequestParam String enteredFileKey) throws Exception {

        // 1. Fetch the specific Medical Record from the database
        // MedicalRecord record = medicalRecordRepository.findById(documentId).orElseThrow();

        // 2. Verify that the user entered the correct File Key
        // String actualFileKey = record.getFileKey();
        String actualFileKey = "5JecV"; // Hardcoded for demonstration

        if (!actualFileKey.equals(enteredFileKey)) {
            return "Access Denied: Invalid Decrypt Secret Key.";
        }

        // 3. If the key matches, decrypt the prescription using AES-128
        // String encryptedData = record.getEncryptedPrescription();
        String encryptedData = "base64EncryptedStringHere..."; // Placeholder
        
        // String decryptedPrescription = encryptionService.decryptAES128(encryptedData);

        return "Decryption successful! Here is your prescription data.";
    }
}