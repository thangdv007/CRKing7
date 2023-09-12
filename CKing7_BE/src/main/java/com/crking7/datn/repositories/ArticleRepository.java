package com.crking7.datn.repositories;

import com.crking7.datn.models.Category;
import com.crking7.datn.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Article findByIdAndStatus(long articleId, int status);
	Page<Article> findAllByStatus(Pageable pageable, int status);
	Page<Article> findAllByCategoryAndStatus(Pageable pageable, Category category, int status);
	Article findByTitle(String title);
	@Query("SELECT a FROM Article a WHERE (:keyword IS NULL OR a.title LIKE %:keyword%)")
    Page<Article> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
	@Query("SELECT a FROM Article a WHERE a.status = 1 ORDER BY a.id DESC")
    List<Article> findByNewest();

	@Query(value = "SELECT * FROM Article a WHERE a.category.id = :categoryId order by rand() limit :limit", nativeQuery = true)
	List<Article> findRelatedArticle(long categoryId, int limit);
}
