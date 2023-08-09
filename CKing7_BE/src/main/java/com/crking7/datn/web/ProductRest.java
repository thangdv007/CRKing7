package com.crking7.datn.web;

import com.crking7.datn.models.Product;
import com.crking7.datn.services.ProductService;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.data.repository.query.Param;
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
    public ResponseEntity<?> getProducts(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        try {
            List<ProductResponse> productResponses = productService.getProducts(pageNo, pageSize, sortBy);
            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Sản phẩm đang được thêm vào!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * get products by categoryId and pagination
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable("id") Long categoryId,
                                                   @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            List<ProductResponse> productResponses = productService.getProductsByCategory(categoryId, pageNo, pageSize, sortBy);

            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào trong danh mục này!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/search")
    public ResponseEntity<?> getProductByName(@RequestParam String keyword,
                                              @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {

        try {
            List<ProductResponse> productResponses = productService.getProductsByKeyword(keyword, pageNo, pageSize, sortBy);

            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi! :" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/searchProduct")
//    public ResponseEntity<?> getProductByColorSizePriceCategory(@Param("valueSize") String valueSize,
//                                                                @Param("valueColor") String valueColor,
//                                                                @Param("minPrice") int minPrice,
//                                                                @Param("maxPrice") int maxPrice,
//                                                                @Param("categoryId") long categoryId,
//                                                                @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
//                                                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                                                @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {
//
//        try {
//            List<ProductResponse> productResponses = productService.getProductByColorSizePriceCategory(valueSize, valueColor, minPrice, maxPrice, categoryId, pageNo, pageSize, sortBy);
//            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào!"));
//        } catch (Exception e) {
//            return new ResponseEntity<>("Lỗi! :" + e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long productId) {
        try {
            ProductResponse productResponse = productService.getProduct(productId);
            return ResponseEntity.ok(Objects.requireNonNullElse(productResponse, "Sản phẩm không tồn tại!"));
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/size/{id}")
    private ResponseEntity<?> getProductBySize(@PathVariable("id") long sizeId) {
        try {
            ProductResponse productResponse = productService.getProductBySize(sizeId);
            return ResponseEntity.ok(Objects.requireNonNullElse(productResponse, "Sản phẩm không tồn tại!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi! :" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/size")
    public ResponseEntity<?> getProductByValueSize(@RequestParam String valueSize,
                                                   @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            List<ProductResponse> productResponses = productService.getProductsByValueSize(valueSize, pageNo, pageSize, sortBy);

            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi! :" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/color")
    public ResponseEntity<?> getProductByValueColor(@RequestParam String valueColor,
                                                    @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            List<ProductResponse> productResponses = productService.getProductsByValueColor(valueColor, pageNo, pageSize, sortBy);

            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi! :" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/price")
    public ResponseEntity<?> getProductByPrice(@RequestParam int minPrice,
                                               @RequestParam int maxPrice,
                                               @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                               @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {

        try {
            List<ProductResponse> productResponses = productService.getProductsByPrice(minPrice, maxPrice, pageNo, pageSize, sortBy);

            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi! :" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/searchPro")
    public ResponseEntity<?> searchProduct(@Param("valueSize") String valueSize,
                                           @Param("valueColor") String valueColor,
                                           @Param("minPrice") Integer minPrice,
                                           @Param("maxPrice") Integer maxPrice,
                                           @Param("categoryId") long categoryId,
                                           @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            List<ProductResponse> productResponses = productService.searchProduct(valueSize, valueColor, minPrice, maxPrice, categoryId, pageNo, pageSize, sortBy);

            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi! :" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sale/{saleId}")
    private ResponseEntity<?> getProductBySale(@PathVariable("saleId") Long saleId,
                                               @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                               @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        try {
            List<ProductResponse> productResponses = productService.getProductBySaleId(saleId, pageNo, pageSize, sortBy);

            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi! :" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/relatedProducts/{id}")
    public ResponseEntity<?> getRelatedProducts(@PathVariable("id") Long categoryId) {

        try {
            List<ProductResponse> productResponses = productService.getRelatedProducts(categoryId, 10);

            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào!"));
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
