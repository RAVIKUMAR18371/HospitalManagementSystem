package com.HMS.HospitalManagementSystem.entities;
 
import jakarta.persistence.*;
 
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;
 
    private String name;
    private String email;
    private String Address;
    private String password;
    private Integer age;
    private String secretId;
    private String status = "PENDING";
 
    // ✅ BUG FIX: Add the OneToOne link to User
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
 
    // ✅ Add getter & setter for user
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
 
    // --- existing getters/setters below (no change needed) ---
    public void setStatus(String status){ this.status = status; }
    public String getStatus(){ return status; }
    public Integer getPatientId(){ return patientId; }
    public void setName(String name){ this.name = name; }
    public String getName(){ return name; }
    public void setEmail(String email){ this.email = email; }
    public String getEmail(){ return email; }
    public void setPassword(String password){ this.password = password; }
    public String getPassword(){ return password; }
    public void setAge(Integer age){ this.age = age; }
    public Integer getAge(){ return age; }
    public void setSecretId(String secretId){ this.secretId = secretId; }
    public String getSecretId(){ return secretId; }
    public void setAddress(String address){ this.Address = address; }
    public String getAddress(){ return Address; }
}