package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.web.dto.request.AddEmpRequest;
import com.crking7.datn.web.dto.response.UserResponse;
import com.crking7.datn.services.UserService;
import com.crking7.datn.web.dto.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class AUserRest {
    @Autowired
    private final UserService userService;

    public AUserRest(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "15") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
        try {
            List<UserResponse> userResponses = userService.getAllUsers(pageNo, pageSize, sortBy, sortDirection.equals("asc"));
            int total = userResponses.size();
            if (userResponses != null && !userResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(userResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "Lấy danh sách thành công", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/hide")
    public ResponseEntity<?> hideUser(@RequestParam(name = "userId") Long userId,
                                      @RequestParam(name = "id") Long id) {
        try {
            String s = userService.hideUser(userId, id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/show")
    public ResponseEntity<?> showUser(@RequestParam(name = "userId") Long userId,
                                      @RequestParam(name = "id") Long id) {
        try {
            String s = userService.showUser(userId, id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addEmp")
    public ResponseEntity<?> addEmP(@RequestBody AddEmpRequest addEmpRequest) {
        try {
            UserResponse userResponse = userService.findByUserName(addEmpRequest.getUsername());
            UserResponse userResponse1 = userService.findByEmail(addEmpRequest.getEmail());
            if (userResponse == null) {
                if (userResponse1 == null){
                    String s = userService.addEmP(addEmpRequest);
                    return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(ApiResponse.build(200, false, "Thành công", "Email đã tồn tại"), HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>(ApiResponse.build(200, false, "Thành công", "Tên tài khoản đã tồn tại"), HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("user/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id") Long userId,
                                        @RequestBody UserRequest userRequest) {
        try {
            String s = userService.updateUser(userId, userRequest);
            if (s != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", s), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Không thành công"), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
