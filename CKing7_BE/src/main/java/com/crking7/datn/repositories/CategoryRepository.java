package com.crking7.datn.repositories;

import com.crking7.datn.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	List<Category> findByStatus(int status);
	Page<Category> findAllByStatus(Pageable pageable, int status);
	List<Category> findAllByStatusAndType(int status, int type);
	Category findByStatusAndIdAndType(int status, long id, int type);
	Category findByTitle(String title);
}
