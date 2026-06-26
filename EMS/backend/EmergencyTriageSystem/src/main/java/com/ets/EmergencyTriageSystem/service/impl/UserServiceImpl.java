package com.ets.EmergencyTriageSystem.service.impl;


import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.UserProfileDTO;
import com.ets.EmergencyTriageSystem.dto.UserRequest;
import com.ets.EmergencyTriageSystem.entity.User;
import com.ets.EmergencyTriageSystem.enums.Role;
import com.ets.EmergencyTriageSystem.exceptions.BadRequestException;
import com.ets.EmergencyTriageSystem.exceptions.NotFoundException;
import com.ets.EmergencyTriageSystem.repo.UserRepository;
import com.ets.EmergencyTriageSystem.security.JwtUtils;
import com.ets.EmergencyTriageSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ets.EmergencyTriageSystem.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public Response<?> signUp(UserRequest userRequest) {
        log.info("Inside signUp()");
        String trimmedUsername = userRequest.getUsername().trim();
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());

        if (existingUser.isPresent()){
            throw new BadRequestException("Username already taken");
        }

        User user = new User();
        user.setUsername(trimmedUsername);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        //save the user
        userRepository.save(user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User Registered Sucessfully")
                .build();

    }

    @Override
    public Response<?> login(UserRequest userRequest) {

        log.info("Inside login()");
        String trimmedUsername = userRequest.getUsername().trim();

        User user = userRepository.findByUsernameIgnoreCase(trimmedUsername)
                .orElseThrow(()-> new NotFoundException("User Not Found"));

        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())){
            throw new BadRequestException("Invalid Password");
        }

        String token = jwtUtils.generateToken(user.getUsername());

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Successful")
                .data(token)
                .build();

    }

    @Override
    public User getCurrentLoggedInUser() {

        String  username = SecurityContextHolder.getContext().getAuthentication().getName();
    log.info("🔍 Looking for user with username: '{}'", username); // Debug
        //return userRepository.findByUsername(username)
        return userRepository.findByUsernameIgnoreCase(username.trim())
                .orElseThrow(()-> new NotFoundException("User not found"));
    }

    @Override
    public UserProfileDTO getCurrentUserProfile() {
        User user = getCurrentLoggedInUser();
        return UserProfileDTO.builder()
                .username(user.getUsername())
                .build();
    }

}
