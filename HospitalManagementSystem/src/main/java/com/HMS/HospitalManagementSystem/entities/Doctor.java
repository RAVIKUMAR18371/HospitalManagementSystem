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

    // BUG FIX #9: Removed the manually written "public Integer getDoctorId()" method.
    // @Getter from Lombok ALREADY generates getDoctorId(). Having both causes a
    // "duplicate method" compile error.

    private String name;
    private String email;
    private String specialization; // e.g., Surgery, Cardiology, General Physician
    private String contact;
}

