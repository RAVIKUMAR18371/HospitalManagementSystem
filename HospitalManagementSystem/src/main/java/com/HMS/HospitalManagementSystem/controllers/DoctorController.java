package com.HMS.HospitalManagementSystem.controllers;

import com.HMS.HospitalManagementSystem.entities.MedicalRecord;
import com.HMS.HospitalManagementSystem.repositories.MedicalRecordRepository;
import com.HMS.HospitalManagementSystem.security.EncryptionService;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// BUG FIX #24: Removed the erroneous import of HospitalManagementSystemApplication.
// That import was completely wrong — a @SpringBootApplication class should never
// be imported inside a controller. It caused a compile warning and could confuse
// the Spring context if misused.

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final EncryptionService encryptionService;
    private final MedicalRecordRepository medicalRecordRepository;

    public DoctorController(EncryptionService encryptionService, MedicalRecordRepository medicalRecordRepository) {
        this.encryptionService = encryptionService;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @PostMapping("/submit-report")
    @PreAuthorize("hasRole('DOCTOR')")
    public String submitPatientReport(
            @RequestParam Integer patientId,
            @RequestParam Integer doctorId,
            @RequestParam String department,
            @RequestParam String description,
            @RequestBody String rawPrescriptionText) throws Exception {

        MedicalRecord record = new MedicalRecord();
        record.setPatientId(patientId);
        record.setDoctorId(doctorId);
        record.setDepartment(department);
        record.setDescription(description);

        // 1. Encrypt the prescription using AES-128 before saving
        String encryptedData = encryptionService.encryptAES128(rawPrescriptionText);

        // BUG FIX #25: Original called record.setEncryptedDescription(encryptedData)
        // but after the MedicalRecord fix, the setter is now correctly named
        // setEncryptedPrescription. Updated to match.
        record.setEncryptedPrescription(encryptedData);

        // 2. Generate SHA-256 hash for data integrity verification
        String hash = encryptionService.generateSHA256Hash(encryptedData);
        record.setDataHash(hash);

        // 3. Generate a unique 8-character File Key for later decryption
        String generatedFileKey = UUID.randomUUID().toString().substring(0, 8);
        record.setFileKey(generatedFileKey);

        medicalRecordRepository.save(record);

        return "Report submitted successfully. Generated File Key for patient: " + generatedFileKey;
    }
}
