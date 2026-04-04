package com.HMS.HospitalManagementSystem.security;

import org.springframework.stereotype.Service;

import lombok.Value;

@Service
public class EncryptionService {
    @Value("${app.aes.key}")
    private String aes_key;

   private static final String AES_KEY;
   
    
}
