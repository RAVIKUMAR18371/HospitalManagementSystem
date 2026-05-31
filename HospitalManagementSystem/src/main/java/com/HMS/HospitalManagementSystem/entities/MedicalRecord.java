// package com.HMS.HospitalManagementSystem.entities;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;


// @Entity
// @Table(name="medical_records")
// public class MedicalRecord {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Integer documentId;

//     private Integer patientId;
//     private Integer doctorId;
//     private String department;
//     private String description; // e.g., "general-checkup"

//     // Stores the AES-128 encrypted prescription
//     /*@Column: This annotation tells the database, "Hey, map this Java variable to a column in my table."
//     columnDefinition = "TEXT": This is the most important part. By default, a Java String maps to a VARCHAR(255). */
//     @Column(columnDefinition = "TEXT")
//     private String encryptedPrescription;


//     // Stores the SHA-256 hash for blockchain-like data integrity
//     private String dataHash;

//     //The specific key required by the patient/staff to decrypt the file later
//     private String fileKey;

//     public Integer getDocumentId(){
//         return documentId;
//     }

//     public void setEncryptedDescription(String encryptedPrescription){
//         this.encryptedPrescription = encryptedPrescription;
//     }

//     public String getEncryptedPrescription(){
//         return encryptedPrescription;
//     }

//     public void setPatientId(Integer PatientId){
//         this.patientId = PatientId;
//     }

//     public Integer getPatientId(){
//         return patientId;
//     }

//      public void setDoctorId(Integer DoctorId){
//         this.doctorId = DoctorId;
//     }

//     public Integer getDoctorId(){
//         return doctorId;
//     }

//       public void setDepartment(String department){
//         this.department = department;
//     }

//     public String getDepartment(){
//         return department;
//     }
    
    
//       public void setDescription(String description){
//         this.description = description;
//     }

//     public String getDescription(){
//         return description;
//     }

//     public void setDataHash(String dataHash){
//         this.dataHash = dataHash;
//     }

//     public String getDataHash(){
//         return dataHash;
//     }

//     public void setFileKey(String fileKey){
//         this.fileKey = fileKey;
//     }

//     public String getFileKey(){
//         return fileKey;
//     }
    
// }

package com.HMS.HospitalManagementSystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer documentId;

    private Integer patientId;
    private Integer doctorId;
    private String department;
    private String description;

    @Column(columnDefinition = "TEXT")
    private String encryptedPrescription;

    private String dataHash;

    private String fileKey;

    public Integer getDocumentId() {
        return documentId;
    }

    // BUG FIX #11: The setter was named "setEncryptedDescription" but the field
    // it sets is "encryptedPrescription". This mismatch means DoctorController's call
    // record.setEncryptedDescription(encryptedData) would work, BUT PatientDashboardController
    // calls record.getEncryptedPrescription() — if someone later tries to call the setter
    // by the field name they get a compile error. Fixed: rename setter to match field.
    public void setEncryptedPrescription(String encryptedPrescription) {
        this.encryptedPrescription = encryptedPrescription;
    }

    public String getEncryptedPrescription() {
        return encryptedPrescription;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileKey() {
        return fileKey;
    }
}

