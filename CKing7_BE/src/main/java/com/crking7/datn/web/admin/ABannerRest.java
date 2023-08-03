package com.crking7.datn.web.admin;

import com.crking7.datn.web.dto.request.BannerRequest;
import com.crking7.datn.web.dto.response.BannerResponse;
import com.crking7.datn.services.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/admin/banner")
public class ABannerRest {
    private final BannerService bannerService;

    @Autowired
    public ABannerRest(BannerService bannerService) {
        this.bannerService = bannerService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createBanner(@RequestBody BannerRequest bannerRequest){
        try {
            BannerResponse bannerResponse = bannerService.createBanner(bannerRequest);
            if (bannerResponse == null) {
                return ResponseEntity.ok("Tên banner đã tồn tại");
            } else {
                return ResponseEntity.ok(bannerResponse);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBannerById(@PathVariable(name = "id") long id){
        try {
            BannerResponse bannerResponse = bannerService.getBannerById(id);
            if(bannerResponse == null){
                return ResponseEntity.ok("Banner không tồn tại");
            }else{
                return ResponseEntity.ok(bannerResponse);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getBanners(@RequestParam(value = "pageNo", defaultValue = "0")int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "20")int pageSize,
                                           @RequestParam(value = "sortBy",defaultValue = "id")String sortBy){
        try {
            List<BannerResponse> bannerResponses = bannerService.getBanners(pageNo, pageSize, sortBy);
            if (bannerResponses != null && !bannerResponses.isEmpty()) {
                return ResponseEntity.ok(bannerResponses);
            } else {
                return ResponseEntity.ok("Banner đang được cập nhật!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi! " + e.getMessage());
        }
    }
    @GetMapping("/allBanner")
    public ResponseEntity<?> getAllBanners(@RequestParam(value = "pageNo", defaultValue = "0")int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "20")int pageSize,
                                        @RequestParam(value = "sortBy",defaultValue = "id")String sortBy){
        try {
            List<BannerResponse> bannerResponses = bannerService.getAllBanners(pageNo, pageSize, sortBy);
            if (bannerResponses != null && !bannerResponses.isEmpty()) {
                return ResponseEntity.ok(bannerResponses);
            } else {
                return ResponseEntity.ok("Banner đang được cập nhật!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi! " + e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBanner(@PathVariable("id") long id,
                                            @RequestBody BannerRequest bannerRequest){
        try{
            BannerResponse bannerResponse = bannerService.updateBanner(id, bannerRequest);
            return ResponseEntity.ok(bannerResponse);
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/hideBanner/{id}")
    public ResponseEntity<?> hideCategory(@PathVariable("id") long id) {
        BannerResponse bannerResponse = bannerService.hideBanner(id);

        if (bannerResponse == null) {
            return new ResponseEntity<>("Không tìm thấy Banner", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Banner đã được ẩn", HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable(name = "id") long id) {
        try {
            bannerService.deleteBanner(id);
            return ResponseEntity.ok("Xóa banner thành công !");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa banner không thành công! Lỗi " + e.getMessage());
        }
    }
}
