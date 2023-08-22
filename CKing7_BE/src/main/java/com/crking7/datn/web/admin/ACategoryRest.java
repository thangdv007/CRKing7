package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.CategoryResponse;
import com.crking7.datn.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
public class ACategoryRest {
    private final CategoryService categoryService;

    @Autowired
    public ACategoryRest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCategory(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        try {
            List<CategoryResponse> categoryResponse = categoryService.getAllCategory(pageNo, pageSize, sortBy);
            if (categoryResponse != null) {
                int total = categoryResponse.size();
                List<Object> data = new ArrayList<>(categoryResponse);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                int total = 0;
                return new ResponseEntity<>(ApiResponsePage.build(201, true, pageNo, pageSize, total, "Lấy danh sách thành công", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
            if (categoryResponse == null) {
                return ResponseEntity.ok("Tên danh mục đã tồn tại");
            } else {
                return ResponseEntity.ok(categoryResponse);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") long id,
                                            @RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest);
            return ResponseEntity.ok(categoryResponse);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/hideCategory/{id}")
    public ResponseEntity<?> hideCategory(@PathVariable("id") long id) {
        CategoryResponse categoryResponse = categoryService.hideCategory(id);

        if (categoryResponse == null) {
            return new ResponseEntity<>("Không tìm thấy danh mục", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Danh mục đã được ẩn", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "id") long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Xóa danh mục thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa danh mục không thành công! Lỗi " + e.getMessage());
        }
    }
}
