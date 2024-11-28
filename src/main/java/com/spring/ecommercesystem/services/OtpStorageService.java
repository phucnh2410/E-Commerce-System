package com.spring.ecommercesystem.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Lazy
public class OtpStorageService {
    private Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    private class OtpData{
        private String otpCode;
        private LocalDateTime expirationTime;

        public OtpData(String otpCode, LocalDateTime expirationTime) {
            this.otpCode = otpCode;
            this.expirationTime = expirationTime;
        }

        public String getOtpCode() {
            return otpCode;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }

    public String generateOtpCode() {
        return String.valueOf((int) ((Math.random() * 900000) + 100000)); // Mã OTP 6 chữ số
    }

    public void storeOtp(String email, String otpCode){
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        otpStorage.put(email, new OtpData(otpCode, expirationTime)); //store otp into Map
    }


    public boolean verifyOtp(String email, String inputOtpCode){
        OtpData otpData = otpStorage.get(email);

        if (otpData != null){
            if (otpData.getOtpCode().equalsIgnoreCase(inputOtpCode) && otpData.getExpirationTime().isAfter(LocalDateTime.now())){//check otp and expiration time
                otpStorage.remove(email);
                return true;
            }
        }

        return false;
    }
























}
