package com.crking7.datn.web;

import com.crking7.datn.models.dtos.SizeDto;
import com.crking7.datn.services.SizeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/size")
public class SizeRest {
    private final SizeService sizeService;

    public SizeRest(SizeService sizeService) {
        this.sizeService = sizeService;
    }
    @GetMapping("")
    public List<SizeDto> getAllSizeValues() {
        return sizeService.getAllValueSize();
    }
}
