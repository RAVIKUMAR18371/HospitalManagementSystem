package com.HMS.HospitalManagementSystem.entities;

import javax.print.DocFlavor.STRING;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   /* @Enumerated(EnumType.STRING) --> Ye JPA ko bol raha hai:
“Is enum ko database mein STRING ke form mein store karo” */
    @Enumerated(EnumType.STRING) // EnumType.ORDINAL --> 0,1,2,3 and EnumType.STRING --> USER, ADMIN, SUPER_ADMIN, DOCTOR
    private RoleEnum name;
    
    
    public RoleEnum getName(){
        return name;
    }

    public void setName(RoleEnum name){
        this.name = name;
    }
}
