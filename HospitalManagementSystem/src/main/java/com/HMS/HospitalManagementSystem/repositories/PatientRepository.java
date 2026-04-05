package com.HMS.HospitalManagementSystem.repositories;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.HMS.HospitalManagementSystem.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient,Integer> {
    // Spring Automatically writes the SQL to find the patients by their status
    List<Patient> findByStatus(String status);
    
}
