package com.crking7.datn.web.admin;

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
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            ProductResponse productResponse = productService.createProduct(productRequest);
            return ResponseEntity.ok(productResponse);
        } catch (Exception e) {
            String errorMessage = "Không thể tạo mới sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductUDRequest productRequest) {
        try {
            ProductResponse productResponse = productService.updateProduct(productRequest);
            if (productResponse == null) {
                return new ResponseEntity<>("Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(productResponse);
        } catch (Exception e) {
            String errorMessage = "Không thể cập nhật sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideProduct(@PathVariable("id") long id) {
        try {
            ProductResponse productResponse = productService.hideProduct(id);
            if (productResponse == null) {
                return new ResponseEntity<>("Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Sản phẩm đã được ẩn", HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Không thể ẩn sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> showProduct(@PathVariable("id") long id) {
        try {
            ProductResponse productResponse = productService.showProduct(id);
            if (productResponse == null) {
                return new ResponseEntity<>("Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Sản phẩm đã được hiện ", HttpStatus.OK);
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
}
