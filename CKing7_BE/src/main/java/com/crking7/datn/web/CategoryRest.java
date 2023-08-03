package com.crking7.datn.web;

import com.crking7.datn.services.CategoryService;
import com.crking7.datn.web.dto.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryRest {
    private final CategoryService categoryService;

    @Autowired
    public CategoryRest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<?> getCategories(@RequestParam(value = "pageNo", defaultValue = "0")int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20")int pageSize,
                                         @RequestParam(value = "sortBy",defaultValue = "id")String sortBy){
        try {
            List<CategoryResponse> categoryResponses = categoryService.getCategories(pageNo, pageSize, sortBy);
            if (categoryResponses != null && !categoryResponses.isEmpty()) {
                return ResponseEntity.ok(categoryResponses);
            } else {
                return ResponseEntity.ok("Danh mục đang được cập nhật!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi! " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable(name = "id") long id){
        try {
            CategoryResponse categoryResponse = categoryService.getCategoryById(id);
            if(categoryResponse == null){
                return ResponseEntity.ok("Banner không tồn tại");
            }else{
                return ResponseEntity.ok(categoryResponse);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getCategoriesByType(@PathVariable("type") int type){
        try {
            List<CategoryResponse> categoryResponses = categoryService.getCategoriesByType(type);
            if (categoryResponses != null && !categoryResponses.isEmpty()) {
                return ResponseEntity.ok(categoryResponses);
            } else {
                return ResponseEntity.ok("Danh mục đang được cập nhật!");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi! " + e.getMessage());
        }
    }
}
