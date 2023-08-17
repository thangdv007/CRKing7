package com.crking7.datn.services.impl;

import com.crking7.datn.exceptions.ResourceNotFoundException;
import com.crking7.datn.repositories.RoleRepository;
import com.crking7.datn.repositories.UserRepository;
import com.crking7.datn.services.UserService;
import com.crking7.datn.utils.EmailUtils;
import com.crking7.datn.utils.OtpService;
import com.crking7.datn.utils.OtpUtil;
import com.crking7.datn.utils.Utils;
import com.crking7.datn.web.dto.request.PasswordRequest;
import com.crking7.datn.web.dto.request.RegisterRequest;
import com.crking7.datn.web.dto.request.UserRequest;
import com.crking7.datn.web.dto.response.UserResponse;
import com.crking7.datn.mapper.UserMapper;
import com.crking7.datn.models.Role;
import com.crking7.datn.models.User;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private Utils utils;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, OtpUtil otpUtil,
                           EmailUtils emailUtils,
                           OtpService otpService,
                           Utils utils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.otpUtil = otpUtil;
        this.emailUtils = emailUtils;
        this.otpService = otpService;
        this.utils = utils;
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
    public String generateOtp(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        try {
            emailUtils.sendOtpEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi otp vui lòng thử lại", e);
        }
        return "Đã lấy mã OTP... Vui lòng xác minh tài khoản trong vòng 5 phút";
    }
    @Override
    @Transactional
    public Object registerUser(RegisterRequest registerRequest) {
        // Kiểm tra trong csdl đã tồn tại người sử dụng có username tương tự user của yêu cầu đăng kí chưa
        User checkUser = userRepository.findByUsername(registerRequest.getUsername());
        User checkUser2 = userRepository.findByEmail(registerRequest.getEmail());
        String otp = otpService.getOtp(registerRequest.getEmail());
        // Kiểm tra mã OTP trong yêu cầu với mã OTP được gửi qua email
        if (registerRequest.getOtp().equals(otp)) {
            if(checkUser != null){
                if(checkUser2 != null){
                    registerRequest.setPassword(passwordEncoder().encode(registerRequest.getPassword()));
                    User user = userMapper.mapSignupToModel(registerRequest);
                    user.setOtpGeneratedTime(LocalDateTime.now());
                    if(Duration.between(user.getOtpGeneratedTime(),
                            LocalDateTime.now()).getSeconds() < (1 * 300)) {
                        Date currentDate = new Date();
                        user.setCreatedDate(currentDate);
                        user.setModifiedDate(currentDate);
                        user.setStatus(1);
                        user.setOtp(otp);
                        // Set default role_id = 2 (ROLE_USER)
                        long defaultRoleId = 2;
                        Role role = roleRepository.findById(defaultRoleId);
                        user.getRoles().add(role);

                        // Lưu thông tin tài khoản mới
                        User newUser = userRepository.save(user);
                        // Xóa mã OTP sau khi đăng ký thành công
                        otpService.removeOtp(registerRequest.getEmail());
                        return userMapper.mapModelToResponse(newUser);
                    }else{
                        return "Mã OTP đã hết hạn. Vui lòng lấy lại mã OTP!";
                    }
                }
                else {
                    return "Email đã tồn tại .";
                }
            }else {
                return "Tài khoản đã tồn tại.";
            }

        } else {
            return "Mã OTP Không hợp lệ!";
        }
    }

    @Override
    public UserResponse updateUser(Long userId, UserRequest userRequest) {
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
    public String changePassword(Long userId, PasswordRequest passwordRequest) {
        User user = userRepository.findById(userId).orElseThrow();
        //Kiểm tra thông tin của passrequest có hợp lệ hay không
        if (passwordRequest.getNewPassword() != null && passwordRequest.getCfNewPassword() != null){
            //kiểm tra 2 pass có trùng nhau không
            if(Objects.equals(passwordRequest.getNewPassword(), passwordRequest.getCfNewPassword())){
                if(passwordEncoder().matches(passwordRequest.getOldPassword(), user.getPassword())){
                    if(!passwordEncoder().matches(passwordRequest.getNewPassword(), user.getPassword())){
                        //cập nhật pass mới
                        user.setPassword(passwordEncoder().encode(passwordRequest.getNewPassword()));
                        //lưu và trả về kết quả
                        userRepository.save(user);
                        return "Thay đổi mật khẩu thành công";
                    }else{
                        return "Mật khẩu mới phải khác mật khẩu cũ";
                    }
                }
                else {
                    return "Mật khẩu cũ không đúng";
                }
            }else {
                return "Mật khẩu xác nhận không chính xác";
            }
        }else {
            return null;
        }
    }

    @Override
    public String forgotPassword(String username) {
        User user = userRepository.findByUsername(username.toString());
        if(user != null) {
            String newPassword = utils.generateRandomPassword();
            user.setPassword(passwordEncoder().encode(newPassword));
            userRepository.save(user);
            try {
                emailUtils.sendPassword(user.getEmail(), newPassword);
            } catch (MessagingException e) {
                throw new RuntimeException("Không thể gửi otp vui lòng thử lại", e);
            }
            return "Mật khẩu đã được gửi về email đã đăng kí tài khoản này của bạn. Vui lòng kiểm tra email của ban !!";
        }
        else {
            return ("Tài khoản không tồn tại");
        }
    }

}
