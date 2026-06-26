package com.ets.EmergencyTriageSystem.controller;

import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.UserRequest;
import com.ets.EmergencyTriageSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ets.EmergencyTriageSystem.dto.UserProfileDTO;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response<?>> signUp(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.signUp(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<?>> login(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.login(userRequest));
    }

        @GetMapping("/user")
    public UserProfileDTO getCurrentUserProfile() {
        return userService.getCurrentUserProfile();
    }


}