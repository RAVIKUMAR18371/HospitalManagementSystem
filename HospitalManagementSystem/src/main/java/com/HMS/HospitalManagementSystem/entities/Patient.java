package com.HMS.HospitalManagementSystem.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;

    private String name;
    private String email;

    private String password;

    private Integer age;

    private String secretId; // It is needed for document verification and it is used for that.

    public Integer getPatientId(){
        return patientId;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public void setAge(Integer age){
        this.age = age;
    }

    public Integer getAge(){
        return age;
    }

    public void setSecretId(String secretId){
        this.secretId = secretId;
    }

    public String getSecretId(){
        return secretId;
    }
    
}
