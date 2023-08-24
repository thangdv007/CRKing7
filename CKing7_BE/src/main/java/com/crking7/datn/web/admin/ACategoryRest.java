package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.CategoryResponse;
import com.crking7.datn.services.CategoryService;
import com.crking7.datn.web.dto.response.ProductResponse;
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
    public ResponseEntity<?> getAllCategory(@RequestParam(required = false) String keyword,
                                            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        try {
            List<CategoryResponse> categoryResponse = categoryService.getAllCategory(keyword, pageNo, pageSize, sortBy);
            if (categoryResponse != null) {
                int total = categoryResponse.size();
                List<Object> data = new ArrayList<>(categoryResponse);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                int total = 0;
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "Lấy danh sách thành công", null), HttpStatus.OK);
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
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Tên danh mục đã tồn tại"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", categoryResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") long id,
                                            @RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryResponse category = categoryService.getCategoryByName(categoryRequest.getTitle());
            CategoryResponse category2 = categoryService.getCategoryAdmin(id);
            if (category != null) {
                if (category2 != null && category2.getId() == category.getId()) {
                    CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest);
                    if (categoryResponse != null) {
                        return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", categoryResponse), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Tên danh mục đã tồn tại"), HttpStatus.OK);
                }
            } else {
                CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest);
                if (categoryResponse != null) {
                    return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", categoryResponse), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/hideCategory/{id}")
    public ResponseEntity<?> hideCategory(@PathVariable("id") long id) {
        try {
            String categoryResponse = categoryService.hideCategory(id);
            if (categoryResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Lấy danh sách thành công", "Không tìm thấy danh mục"), HttpStatus.OK);
            }
            return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy danh sách thành công", categoryResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/showCategory/{id}")
    public ResponseEntity<?> showCategory(@PathVariable("id") long id) {
        try {
            String categoryResponse = categoryService.showCategory(id);
            if (categoryResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Lấy danh sách thành công", "Không tìm thấy danh mục"), HttpStatus.OK);
            }
            return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy danh sách thành công", categoryResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "id") long id) {
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", "Xóa danh mục thành công"), HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa danh mục không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryAdmin(@PathVariable(name = "id") long id) {
        try {
            CategoryResponse categoryResponse = categoryService.getCategoryAdmin(id);
            if (categoryResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Lấy dữ liệu thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy dữ liệu thành công", categoryResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/parentCategory")
    public ResponseEntity<?> getParentCategory() {
        try {
            List<CategoryResponse> categoryResponse = categoryService.getParentCategory();
            if (categoryResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Lấy dữ liệu thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy dữ liệu thành công", categoryResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
