package com.crking7.datn.web;

import com.crking7.datn.web.dto.response.BannerResponse;
import com.crking7.datn.services.BannerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/banner")
public class BannerRest {
    private final BannerService bannerService;

    public BannerRest(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    /**
     * get banner at home page
     */
    @GetMapping("")
    public ResponseEntity<?> getBanners(@RequestParam(name = "q", defaultValue = "3") int number){
        try{
            List<BannerResponse> bannerResponses = bannerService.getNumberOfBanners(number);
            return ResponseEntity.ok(Objects.requireNonNullElse(bannerResponses, "Lỗi tải banner!"));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getBannersByCategory(@PathVariable("id") long categoryId){
        try{
            List<BannerResponse> bannerResponses = bannerService.getNumberOfBannersByCategory(categoryId, 3);
            return ResponseEntity.ok(Objects.requireNonNullElse(bannerResponses, "Lỗi tải banner!"));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }
}
