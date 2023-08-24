package com.crking7.datn.repositories;

import com.crking7.datn.models.Category;
import com.crking7.datn.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	List<Category> findByStatus(int status);
	Page<Category> findAllByStatus(Pageable pageable, int status);
	List<Category> findAllByStatusAndType(int status, int type);
	Category findByStatusAndIdAndType(int status, long id, int type);
	Category findByTitle(String title);
	@Query("select c from Category c where (:keyword is null or c.title like %:keyword%)")
	Page<Category> getAllByKeyword(@Param("keyword") String keyword,
								  Pageable pageable);
	@Query("SELECT c FROM Category c WHERE c.parentCategory IS NULL")
	List<Category> findAllWithNoParentCategory();

	Category findByIdAndStatus(long id, int status);
}
