package com.HMS.HospitalManagementSystem.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HMS.HospitalManagementSystem.entities.MedicalRecord;
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,Integer> {
    // Here i can say that Spring again write the SQL to automatically fetch for a specific patient
    List<MedicalRecord> findByPatientId(Integer patientId);

}
