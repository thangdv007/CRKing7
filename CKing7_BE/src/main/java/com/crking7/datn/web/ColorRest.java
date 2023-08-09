package com.crking7.datn.web;

import com.crking7.datn.models.dtos.ColorDto;
import com.crking7.datn.models.dtos.SizeDto;
import com.crking7.datn.services.ColorService;
import com.crking7.datn.services.SizeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/color")
public class ColorRest {
    private final ColorService colorService;

    public ColorRest(ColorService colorService) {
        this.colorService = colorService;
    }
    @GetMapping("")
    public List<ColorDto> getAllColorValues() {
        return colorService.getAllValueColor();
    }
}
