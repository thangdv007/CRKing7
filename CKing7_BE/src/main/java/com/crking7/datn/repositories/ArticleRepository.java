package com.crking7.datn.repositories;

import com.crking7.datn.models.Category;
import com.crking7.datn.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Article findByIdAndStatus(long articleId, int status);
	Page<Article> findAllByStatus(Pageable pageable, int status);
	Page<Article> findAllByCategoryAndStatus(Pageable pageable, Category category, int status);
	Article findByTitle(String title);
}
