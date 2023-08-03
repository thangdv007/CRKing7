package com.crking7.datn.web.admin;

import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.CategoryResponse;
import com.crking7.datn.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/category")
public class ACategoryRest {
    private final CategoryService categoryService;

    @Autowired
    public ACategoryRest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        try {
            CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
            if (categoryResponse == null) {
                return ResponseEntity.ok("Tên danh mục đã tồn tại");
            } else {
                return ResponseEntity.ok(categoryResponse);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") long id,
                                           @RequestBody CategoryRequest categoryRequest){
        try{
            CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest);
            return ResponseEntity.ok(categoryResponse);
        }catch (Exception e){
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
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa danh mục không thành công! Lỗi " + e.getMessage());
        }
    }
}
