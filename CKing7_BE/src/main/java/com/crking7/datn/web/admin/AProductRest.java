package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.services.ProductService;
import com.crking7.datn.web.dto.request.ProductRequest;
import com.crking7.datn.web.dto.request.ProductUDRequest;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/admin/product")
public class AProductRest {
    private final ProductService productService;

    public AProductRest(ProductService productService) {
        this.productService = productService;
    }

    /**
     * create product
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductAdmin(@PathVariable("id") Long productId) {
        try {
            ProductResponse productResponse = productService.getProductAdmin(productId);
            if (productResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            ProductResponse productResponse = productService.createProduct(productRequest);
            if (productResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Tạo mới không thành công"), HttpStatus.OK);
            }
        } catch (Exception e) {
            String errorMessage = "Không thể tạo mới sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductUDRequest productRequest) {
        try {
            ProductResponse productResponse = productService.updateProduct(productRequest);
            if (productResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Cập nhật không thành công"), HttpStatus.OK);
            }
        } catch (Exception e) {
            String errorMessage = "Không thể cập nhật sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideProduct(@PathVariable("id") long id) {
        try {
            ProductResponse productResponse = productService.hideProduct(id);
            if (productResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Ẩn sản phẩm không thành công"), HttpStatus.OK);
            }
        } catch (Exception e) {
            String errorMessage = "Không thể ẩn sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> showProduct(@PathVariable("id") long id) {
        try {
            ProductResponse productResponse = productService.showProduct(id);
            if (productResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Hiện sản phẩm không thành công"), HttpStatus.OK);
            }
        } catch (Exception e) {
            String errorMessage = "Không thể hiện sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Xóa sản phẩm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa sản phẩm không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/quantity")
    public ResponseEntity<?> getProductByQuantity(@RequestParam(value = "isActive", defaultValue = "true") boolean isActive,
                                                  @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                  @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            List<ProductResponse> productResponses = productService.getProductByQuantity(isActive, pageNo, pageSize, sortBy);
            if (productResponses != null) {
                int total = productResponses.size();
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                int total = 0;
                return new ResponseEntity<>(ApiResponsePage.build(201, true, pageNo, pageSize, total, "Lấy danh sách thành công", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(@RequestParam(required = false) String keyword,
                                            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            List<ProductResponse> productResponses = productService.getAllProducts(keyword, pageNo, pageSize, sortBy);
            int total = productResponses.size();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "Lấy danh sách thành công", Collections.singletonList("Không có sản phẩm nào trùng khớp")), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponsePage.builder(500, false, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
