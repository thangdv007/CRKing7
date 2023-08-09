package com.crking7.datn.services.impl;

import com.crking7.datn.mapper.ColorMapper;
import com.crking7.datn.mapper.SizeMapper;
import com.crking7.datn.models.dtos.ColorDto;
import com.crking7.datn.models.dtos.SizeDto;
import com.crking7.datn.repositories.ColorRepository;
import com.crking7.datn.repositories.SizeRepository;
import com.crking7.datn.services.ColorService;
import com.crking7.datn.services.SizeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    public ColorServiceImpl(ColorRepository colorRepository,
                            ColorMapper colorMapper) {
        this.colorRepository = colorRepository;
        this.colorMapper = colorMapper;
    }

    @Override
    public List<ColorDto> getAllValueColor() {
        List<String> distinctColorValues = colorRepository.findDistinctColorValues();
        return distinctColorValues.stream()
                .map(ColorDto::new)
                .collect(Collectors.toList());
    }
}
