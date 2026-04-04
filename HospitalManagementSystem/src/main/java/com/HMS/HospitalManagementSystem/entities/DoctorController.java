package com.HMS.HospitalManagementSystem.entities;

import com.HMS.HospitalManagementSystem.HospitalManagementSystemApplication;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.HMS.HospitalManagementSystem.security.EncryptionService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final EncryptionService encryptionService;
    
    public DoctorController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
    
    // Accessible Only by an approved DOCTOR
    @PostMapping("/submit-report")
    @PreAuthorize("hasRole('DOCTOR')")
    public String submitPatientReport(
        @RequestParam Integer patientId,
        @RequestParam Integer doctorId,
        @RequestParam String department,
        @RequestParam String description,
        @RequestBody String rawPrescriptionText) throws Exception{
            MedicalRecord record = new MedicalRecord();
            record.setPatientId(patientId);
            record.setDoctorId(doctorId);
            record.setDepartment(department);
            record.setDescription(description);

            //1. Encrypt the prescription using AES-128 before saving
            String encryptedData = encryptionService.encryptAES128(rawPrescriptionText);
            record.setEncryptedDescription(encryptedData);

            //2.  Generate SHA-256 hash to verify data integrity against unauthorized modifications
            String hash = encryptionService.generateSHA256Hash(encryptedData);
            record.setDataHash(hash);

            // 3. Generate a unique File Key (Decryption Key)
            // This Key is created during approval/submission and kepy safe in the database and only shared with the patient and doctor for later decryption of the prescription.
            String generatedFileKey = UUID.randomUUID().toString().substring(0,8);// Generate a random 8-character key for decrytion.
            record.setFileKey(generatedFileKey);

            // medicalRecordRepository.save(record);

            // In a real application i would return the record details for confirmation and also share the fileKey securely with the patient and doctor for later use in decryption of the prescription.hospitalManagementSystemApplication
            return "Success, I have successfully sent the patient details.";
        }
    
}

