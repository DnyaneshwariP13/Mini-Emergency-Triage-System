package com.ets.EmergencyTriageSystem.service;

import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.UserProfileDTO;
import com.ets.EmergencyTriageSystem.dto.UserRequest;
import com.ets.EmergencyTriageSystem.entity.User;



public interface UserService {

    Response<?> signUp(UserRequest userRequest);
    Response<?> login(UserRequest userRequest);
    User getCurrentLoggedInUser();
    UserProfileDTO getCurrentUserProfile();

}