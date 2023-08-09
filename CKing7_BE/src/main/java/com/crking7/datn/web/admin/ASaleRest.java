package com.crking7.datn.web.admin;

import com.crking7.datn.models.Product;
import com.crking7.datn.services.ArticleService;
import com.crking7.datn.services.SaleService;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.request.BannerRequest;
import com.crking7.datn.web.dto.request.SaleRequest;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.web.dto.response.BannerResponse;
import com.crking7.datn.web.dto.response.SaleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sale")
public class ASaleRest {
    private final SaleService saleService;

    public ASaleRest(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSale(@RequestBody SaleRequest saleRequest){
        try {
            SaleResponse saleResponse = saleService.create(saleRequest);
            return ResponseEntity.ok(saleResponse);
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNo", defaultValue = "0")int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "20")int pageSize,
                                        @RequestParam(value = "sortBy",defaultValue = "id")String sortBy){
        try {
            List<SaleResponse> saleResponses = saleService.getAll(pageNo, pageSize, sortBy);
            if (saleResponses != null && !saleResponses.isEmpty()) {
                return ResponseEntity.ok(saleResponses);
            } else {
                return ResponseEntity.ok("Sale đang được cập nhật!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi! " + e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id,
                                          @RequestBody SaleRequest saleRequest){
        try{
            SaleResponse saleResponse = saleService.update(id, saleRequest);
            return ResponseEntity.ok(saleResponse);
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        try {
            saleService.delete(id);
            return ResponseEntity.ok("Xóa chương trình sale thành công !");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa chương trình sale không thành công! Lỗi " + e.getMessage());
        }
    }
    @PostMapping("/add-product/{id}")
    public ResponseEntity<String> addProductsToSale(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "productIds") List<Long> productIds) {

        try {
            saleService.addProductsToSale(id, productIds);
            return ResponseEntity.ok("Thêm sản phẩm vào chương trình khuyến mãi thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể thêm sản phẩm . Lỗi : " + e.getMessage());
        }
    }
    @PostMapping("/remove-product/{id}")
    public ResponseEntity<String> removeProductsFromSale(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "productIds") List<Long> productIds) {

        try {
            saleService.removeProductsFromSale(id, productIds);
            return ResponseEntity.ok("Xóa sản phẩm khỏi chương trình khuyến mãi thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể xóa sản phẩm . Lỗi : " + e.getMessage());
        }
    }
}