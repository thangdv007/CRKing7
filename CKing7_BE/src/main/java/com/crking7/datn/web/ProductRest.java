package com.crking7.datn.web;

import com.crking7.datn.services.ProductService;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/product")
public class ProductRest {
    private final ProductService productService;
    public ProductRest(ProductService productService) {
        this.productService = productService;
    }

    /**
     * get products and pagination
     */
    @GetMapping("")
    public ResponseEntity<?> getProducts(@RequestParam(value = "pageNo", defaultValue = "0")int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20")int pageSize,
                                         @RequestParam(value = "sortBy",defaultValue = "id")String sortBy){
        try {
            List<ProductResponse> productResponses = productService.getProducts(pageNo, pageSize, sortBy);
            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Sản phẩm đang được thêm vào!"));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * get products by categoryId and pagination
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable("id")Long categoryId,
                                                   @RequestParam(value = "pageNo", defaultValue = "0")int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "20")int pageSize,
                                                   @RequestParam(value = "sortBy",defaultValue = "id")String sortBy){

        try{
            List<ProductResponse> productResponses = productService.getProductsByCategory(categoryId, pageNo, pageSize, sortBy);

            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào trong danh mục này!"));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long productId){
        try{
            ProductResponse productResponse = productService.getProduct(productId);
            return ResponseEntity.ok(Objects.requireNonNullElse(productResponse, "Sản phẩm không tồn tại!"));
        }catch (Exception e){
            return null;
        }
    }

    @GetMapping("/size/{id}")
    private ResponseEntity<?> getProductBySize(@PathVariable("id") long sizeId){
        try {
            ProductResponse productResponse = productService.getProductBySize(sizeId);
            return ResponseEntity.ok(Objects.requireNonNullElse(productResponse, "Sản phẩm không tồn tại!"));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }
}
