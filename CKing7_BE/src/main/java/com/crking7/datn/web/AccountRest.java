package com.crking7.datn.web;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.models.User;
import com.crking7.datn.web.dto.request.ForgotPassRequest;
import com.crking7.datn.web.dto.request.LoginRequest;
import com.crking7.datn.web.dto.request.PasswordRequest;
import com.crking7.datn.web.dto.request.RegisterRequest;
import com.crking7.datn.web.dto.response.LoginResponse;
import com.crking7.datn.web.dto.response.UserResponse;
import com.crking7.datn.securities.JwtConfig;
import com.crking7.datn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountRest {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final UserService userService;

    @Autowired
    public AccountRest(AuthenticationManager authenticationManager, JwtConfig jwtConfig, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        try {
            UserResponse userResponse = userService.findByUserName(loginRequest.getUsername());
            if (userResponse.getStatus() != 0) {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                //get token form tokenProvider
                String token = jwtConfig.generateToken(authentication);

                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setToken(token);
                loginResponse.setUser(userResponse);

                return ResponseEntity.ok(loginResponse);
            } else {
                return new ResponseEntity<>("Tài khoản đã bị khoá!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Tài khoản hoặc mật khẩu không chính xác!", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        try {
            Object userResponse = userService.registerUser(registerRequest);
            if (userResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", userResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Đăng ký không thành công", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changePassword(@PathVariable(name = "id") Long userId,
                                            @RequestBody PasswordRequest passwordRequest){
        try {
            String changePasswordResult = userService.changePassword(userId, passwordRequest);
            if (changePasswordResult.equals("Thay đổi mật khẩu thành công")) {
                return new ResponseEntity<>(ApiResponse.build(200, true, changePasswordResult, changePasswordResult), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Thay đổi mật khẩu không thành công", changePasswordResult), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/generate-otp")
    public ResponseEntity<?> regenerateOtp(@RequestBody RegisterRequest registerRequest) {
        try {
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", userService.generateOtp(registerRequest)), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("user/forgotPass")
    public ResponseEntity<?> forgotPassword(@RequestParam String username) {
        try {
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", userService.forgotPassword(username)), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
