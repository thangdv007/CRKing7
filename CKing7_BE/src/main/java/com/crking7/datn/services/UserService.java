package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.PasswordRequest;
import com.crking7.datn.web.dto.request.RegisterRequest;
import com.crking7.datn.web.dto.request.UserRequest;
import com.crking7.datn.web.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    /**
     *
     */
    Page<UserResponse> getAllUsers(int pageNumber, int pageSize, String sortBy);
    UserResponse findByUserName(String username);
    UserResponse getUser(long userId);
    UserResponse registerUser(RegisterRequest registerRequest);
    UserResponse updateUser(Long userId, UserRequest userRequest);

    UserResponse changePassword(Long userId, PasswordRequest passwordRequest);


}
