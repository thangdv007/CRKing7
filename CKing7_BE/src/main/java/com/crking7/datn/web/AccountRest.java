package com.crking7.datn.web;

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
            UserResponse userResponse = userService.registerUser(registerRequest);
            if (userResponse != null) {
                return ResponseEntity.ok(userResponse);
            } else {
                return new ResponseEntity<>("Tên tài khoản đã tồn tại!", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Email hoặc số điện thoại đã được sử dụng.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<?> changePassword(@PathVariable(name = "id") Long userId,
                                            @RequestBody PasswordRequest passwordRequest){
        UserResponse userResponse = userService.changePassword(userId,passwordRequest);
        if(userResponse != null){
            return ResponseEntity.ok(userResponse);
        }
        return new ResponseEntity<>("Thay đổi mật khẩu không thành công", HttpStatus.BAD_REQUEST);
    }
}
