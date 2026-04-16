package com.cloudportal.jobportal.controller;

import com.cloudportal.jobportal.model.User;
import com.cloudportal.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    // ================= SEND OTP =================
    @PostMapping("/send-otp")
public String sendOtp(@RequestBody Map<String,String> req){

    String email = req.get("email");

    userService.sendOtpEmail(email);

    return "OTP sent successfully";
}


    // ================= SIGNUP WITH OTP =================
    @PostMapping("/signup")
    public User signup(@RequestBody Map<String,String> req){

        String email = req.get("email");
        String password = req.get("password");
        String otp = req.get("otp");

        // OTP verify
        if(!userService.verifyOtp(email, otp)){
            return null;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        return userService.registerUser(user);
    }


    // ================= LOGIN (PASSWORD) =================
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.loginUser(user.getEmail(), user.getPassword());
    }


    // ================= LOGIN WITH OTP =================
    @PostMapping("/login-otp")
    public User loginWithOtp(@RequestBody Map<String,String> req){

        String email = req.get("email");
        String otp = req.get("otp");

        return userService.loginWithOtp(email, otp);
    }


    // ================= GET USER =================
    @GetMapping("/{email}")
    public User getUser(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }


    // ================= UPDATE PROFILE =================
    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
}