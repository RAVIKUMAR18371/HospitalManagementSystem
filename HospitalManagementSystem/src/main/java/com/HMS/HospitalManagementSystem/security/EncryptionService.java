package com.HMS.HospitalManagementSystem.security;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private static final String AES_KEY = "YourKey16Secrets";

    //Encrypt prescriptions and patient details using AES-128
    public String encryptAES128(String data) throws Exception{
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES"); // Cipher is used for encryption and decryption
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Hash records using SHA-256 to prevent tempering
    public String generateSHA256Hash(String data) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // MessageDigest is used for hashing and how -> it works is that it takes input data and produces a fixed-size hash value, which is unique to the input data. It is a one-way function, meaning that it is computationally infeasible to reverse the process and obtain the original input data from the hash value.
        byte[] hash = digest.digest(data.getBytes("UTF-8")); //it works is that it takes input data and produces a fixed-size hash value, which is unique to the input data. It is a one-way function, meaning that it is computationally infeasible to reverse the process and obtain the original input data from the hash value.
        StringBuilder hexString = new StringBuilder();
        for(byte b : hash){
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1){
                hexString.append('0');
                
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Decrypt the AES-128 encrypted prescriptions
    public String decryptAES128(String encryptedData) throws Exception{
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decode from Base64, then decrypt
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
   
    
}
