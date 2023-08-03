package com.crking7.datn.repositories;

import com.crking7.datn.models.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {

    List<Color> findByProductId(long productId);

}
