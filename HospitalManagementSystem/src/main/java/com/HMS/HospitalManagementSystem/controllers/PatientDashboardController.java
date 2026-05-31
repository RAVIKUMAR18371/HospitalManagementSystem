// package com.HMS.HospitalManagementSystem.controllers;
// import java.util.*;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.HMS.HospitalManagementSystem.entities.MedicalRecord;
// import com.HMS.HospitalManagementSystem.repositories.MedicalRecordRepository;
// import com.HMS.HospitalManagementSystem.security.EncryptionService;

// @RestController
// @RequestMapping("/api/patient-dashboard")
// public class PatientDashboardController {

//     private final MedicalRecordRepository recordRepository;
//     private final EncryptionService encryptionService;

//     // Injectting the repository via constructor
//     public PatientDashboardController(MedicalRecordRepository recordRepository, EncryptionService encryptionService){
//         this.recordRepository = recordRepository;
//         this.encryptionService = encryptionService;
//     }

//     // Step 1: Display the Approved List of Documents
//     @GetMapping("/{patientId}/approved-list")
//     @PreAuthorize("hasRole('USER')") // Only accessible by patients
//     public List<MedicalRecord> getApprovedList(@PathVariable Integer patientId) {
//         // In a real application, you would query the database:
//         // List<MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId);
//         // The table must return data such as Patient ID, Doctor ID, Department, Description, File Key, and an Action button [1].
//         return recordRepository.findByPatientId(patientId);
//     }

//     // Step 2: Verify the File Key and Decrypt the Document
//     @PostMapping("/download")
//     @PreAuthorize("hasRole('USER')")
//     public String downloadAndDecryptDocument(@RequestParam Integer documentId, @RequestParam String enteredFileKey) throws Exception{
//         // Fetch the Specific Medical Record from the database
//         // Using 'recordRepository' to match your constructor injection
//         MedicalRecord record = recordRepository.findById(documentId)
//         .orElseThrow(() -> new RuntimeException("Medical Record not found."));


//         // 2. Retrieve the actual File Key securely stored in the database
//         String actualFileKey = record.getFileKey();

//         // 3. Verify that the user entered the correct File Key
//         if(!actualFileKey.equals(enteredFileKey)){
//             return "Access Denied: Invalid Decrypt Secret Key.";
//         }

//         // 4. If the Key matches, retrieve the AES-128 encrypted prescription from the database
//         String encryptedData = record.getEncryptedPrescription();

//         // 5. I am Decrypting the Prescription back into readable text
//         String decryptedPrescription = encryptionService.decryptAES128(encryptedData);

//         // 6. Return the actual decrypted medical data to the patient
//         return "Decryption successful! Here is your prescription data:\n\n" + decryptedPrescription;
//     }

// }



package com.HMS.HospitalManagementSystem.controllers;

import java.util.List;

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

    public PatientDashboardController(MedicalRecordRepository recordRepository, EncryptionService encryptionService) {
        this.recordRepository = recordRepository;
        this.encryptionService = encryptionService;
    }

    // Step 1: Get all medical records for a patient
    @GetMapping("/{patientId}/approved-list")
    @PreAuthorize("hasRole('USER')")
    public List<MedicalRecord> getApprovedList(@PathVariable Integer patientId) {
        return recordRepository.findByPatientId(patientId);
    }

    // Step 2: Verify File Key and decrypt the prescription
    @PostMapping("/download")
    @PreAuthorize("hasRole('USER')")
    public String downloadAndDecryptDocument(
            @RequestParam Integer documentId,
            @RequestParam String enteredFileKey) throws Exception {

        MedicalRecord record = recordRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Medical Record not found."));

        String actualFileKey = record.getFileKey();

        if (!actualFileKey.equals(enteredFileKey)) {
            return "Access Denied: Invalid File Key.";
        }

        String encryptedData = record.getEncryptedPrescription();
        String decryptedPrescription = encryptionService.decryptAES128(encryptedData);

        return "Decryption successful! Here is your prescription:\n\n" + decryptedPrescription;
    }
}
