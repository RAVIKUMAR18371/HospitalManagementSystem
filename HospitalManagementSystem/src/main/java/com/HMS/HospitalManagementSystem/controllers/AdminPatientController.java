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

    public AdminPatientController(PatientRepository patientRepository, EncryptionService encryptionService){
        this.encryptionService = encryptionService;
        this.patientRepository = patientRepository;
    }

    // Now --> I am going to use the PUT method for updating the record
    @PutMapping("/update/{patientId}")
    @PreAuthorize("hasRole('ADMIN')") // only the Admin can update
    public String updatePatient(@PathVariable Integer patientId, @RequestBody Patient updatedData) throws Exception{
        // Fetch the existing patient from the DB
        Patient existingPatient = patientRepository.findById(patientId)
                                                    .orElseThrow(() -> new RuntimeException("Patient not found"));

    // Updating the Allowed Fields
    existingPatient.setAge(updatedData.getAge());
    existingPatient.setAddress(updatedData.getAddress());

    // If a sensitive field is updated, I am encrypting again before Saving
    if(updatedData.getSecretId() != null){
        existingPatient.setSecretId(encryptionService.encryptAES128(updatedData.getSecretId()));
    }

    // Now I am saving the updated Record in Postgre
    patientRepository.save(existingPatient);

    return "I have updated and secured done successfully";

    }


    // Delete Method --> Removing the Patient from the system
    @DeleteMapping("/delete/{patientId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deletePatient(@PathVariable Integer patientId){
        // First I will check that the Patient exist or not
        if(!patientRepository.existsById(patientId)){
            return "Error: Patient does not exist.";
        }

        // Actually delete the row from the Postgres database
        patientRepository.deleteById(patientId);

        return "Patient successfully deleted from the system.";

    }


    
}
