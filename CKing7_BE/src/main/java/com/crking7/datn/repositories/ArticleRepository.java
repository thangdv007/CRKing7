package com.crking7.datn.repositories;

import com.crking7.datn.models.Category;
import com.crking7.datn.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Article findByIdAndStatus(long articleId, int status);
	Page<Article> findAllByStatus(Pageable pageable, int status);
	Page<Article> findAllByCategoryAndStatus(Pageable pageable, Category category, int status);
	Article findByTitle(String title);
	@Query("SELECT a FROM Article a WHERE (:keyword IS NULL OR a.title LIKE %:keyword%)")
    Page<Article> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
