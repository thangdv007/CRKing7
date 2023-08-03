package com.crking7.datn.services.impl;

import com.crking7.datn.exceptions.ResourceNotFoundException;
import com.crking7.datn.repositories.RoleRepository;
import com.crking7.datn.repositories.UserRepository;
import com.crking7.datn.services.UserService;
import com.crking7.datn.web.dto.request.PasswordRequest;
import com.crking7.datn.web.dto.request.RegisterRequest;
import com.crking7.datn.web.dto.request.UserRequest;
import com.crking7.datn.web.dto.response.UserResponse;
import com.crking7.datn.mapper.UserMapper;
import com.crking7.datn.models.Role;
import com.crking7.datn.models.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public Page<UserResponse> getAllUsers(int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy).ascending());
        return userRepository.findAll(pageable).map(userMapper::mapModelToResponse);
    }

    @Override
    public UserResponse findByUserName(String username) {
        return userMapper.mapModelToResponse(userRepository.findByUsername(username));
    }

    @Override
    public UserResponse getUser(long userId) {
        return userMapper.mapModelToResponse(userRepository.findById(userId).orElseThrow());
    }


    @Override
    @Transactional
    public UserResponse registerUser(RegisterRequest registerRequest) {

        // Kiểm tra trong csdl đã tồn tại người sử dụng có username tương tự user của yêu cầu đăng kí chưa
        User checkUser = userRepository.findByUsername(registerRequest.getUsername());

        if(checkUser == null){
            // Set new information
            registerRequest.setPassword(passwordEncoder().encode(registerRequest.getPassword()));
            User user = userMapper.mapSignupToModel(registerRequest);

            user.setStatus(1);
            Date currentDate = new Date();
            user.setCreatedDate(currentDate);
            user.setModifiedDate(currentDate);

            // Set default role_id = 2 (ROLE_USER)
            long defaultRoleId = 2;
            Role role = roleRepository.findById(defaultRoleId);
            user.getRoles().add(role);

            User user1 = userRepository.save(user);
            return userMapper.mapModelToResponse(user1);
        }else {
            return null;
        }
    }

    @Override
    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        //Kiểm tra cfPassword có trùng với Password nhập vào không
        if (userId != null && userRequest != null){
            //thực hiện lấy thông tin cũ
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

            //update thông tin cho user
            user.setModifiedDate(new Date());
            userMapper.updateModel(user,userRequest);

            //trả về thông tin người sử dụng vừa cập nhật
            return userMapper.mapModelToResponse(userRepository.save(user));
        }
        return null;
    }

    @Override
    public UserResponse changePassword(Long userId, PasswordRequest passwordRequest) {
        //Kiểm tra thông tin của passrequest có hợp lệ hay không
        if (passwordRequest.getCfPassword() != null && passwordRequest.getPassword() != null){
            //kiểm tra 2 pass có trùng nhau không
            if(Objects.equals(passwordRequest.getPassword(), passwordRequest.getCfPassword())){
                //lấy thông tin user
                User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id", userId));

                //cập nhật pass mới
                user.setPassword(passwordEncoder().encode(passwordRequest.getPassword()));

                //lưu và trả về kết quả
                return userMapper.mapModelToResponse(userRepository.save(user));
            }else {
                return null;
            }
        }else {
            return null;
        }

    }
}
