package com.cloudportal.jobportal.service;

import com.cloudportal.jobportal.model.User;
import com.cloudportal.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    // 🔥 OTP STORAGE
    private Map<String, String> otpStorage = new HashMap<>();


    // ================= OTP GENERATE =================
    public String generateOtp(){
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }


    // ================= SEND OTP EMAIL =================
    public void sendOtpEmail(String email){

        String otp = generateOtp();
        otpStorage.put(email, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP Verification - Carelyn Job Portal");
        message.setText("Your OTP is: " + otp);

        mailSender.send(message);
    }


    // ================= VERIFY OTP =================
    public boolean verifyOtp(String email, String otp){
        return otpStorage.containsKey(email) && otpStorage.get(email).equals(otp);
    }


    // ================= SIGNUP =================
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    // ================= LOGIN (PASSWORD) =================
    public User loginUser(String email, String password) {

    email = email.trim().toLowerCase();
    password = password.trim();

    Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);

    if (optionalUser.isPresent()) {
        User user = optionalUser.get();

        System.out.println("Entered password: " + password);
        System.out.println("DB password: " + user.getPassword());

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            System.out.println("Password mismatch ❌");
        }
    } else {
        System.out.println("User not found ❌");
    }

    return null;
}


    // ================= LOGIN (OTP) =================
    public User loginWithOtp(String email, String otp){

        if(verifyOtp(email, otp)){
            return getUserByEmail(email);
        }

        return null;
    }


    // ================= GET USER =================
    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }


    // ================= UPDATE =================
    public User updateUser(User updatedUser) {
        return userRepository.save(updatedUser);
    }
}