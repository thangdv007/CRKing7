package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    /**
     *
     */
    List<UserResponse> getAllUsers(int pageNo, int pageSize, String sortBy, boolean asc);
    String hideUser(Long userId, Long id);
    String addEmP(AddEmpRequest addEmpRequest);
    String showUser(Long userId, Long id);
    UserResponse findByUserName(String username);
    UserResponse findByEmail(String email);
//    Object login(LoginRequest loginRequest);
    UserResponse getUser(long userId);
    Object registerUser(RegisterRequest registerRequest);
    String updateUser(Long userId, UserRequest userRequest);
    String updateProfile(Long userId, UserRequest userRequest);
    String changePassword(Long userId, PasswordRequest passwordRequest);
    String forgotPassword(String username);
    String generateOtp (RegisterRequest registerRequest);

}
