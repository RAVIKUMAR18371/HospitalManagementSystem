package com.HMS.HospitalManagementSystem.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorId;
    public Integer getDoctorId(){
        return doctorId;
    }

    private String name;
    private String email;
    private String specialization; // like --> Surgery, Cardio, General Physian
    private String contact;


    
}
