package com.crking7.datn.repositories;

import com.crking7.datn.models.Banner;
import com.crking7.datn.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
	Page<Banner> findByCategoryAndStatus(Pageable pageable, Category category, int status);
	Page<Banner> findByStatus(Pageable pageable, int status);
	Banner findByName(String name);
}
