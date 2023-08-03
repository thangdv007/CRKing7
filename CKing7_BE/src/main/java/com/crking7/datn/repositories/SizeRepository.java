package com.crking7.datn.repositories;

import com.crking7.datn.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Long> {
    List<Size> findByColorId(long colorId);
}
