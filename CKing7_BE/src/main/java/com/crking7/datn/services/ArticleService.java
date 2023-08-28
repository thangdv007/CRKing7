package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.request.ArticleUDRequest;
import com.crking7.datn.web.dto.response.ArticleResponse;

import java.util.List;

public interface ArticleService {
    ArticleResponse getArticle(long articleId);

    ArticleResponse getArticleAdmin(long articleId);

    ArticleResponse getArticleByName(String title);

    List<ArticleResponse> getArticles(int pageNo, int pageSize, String sortBy);

    List<ArticleResponse> getAllArticles(String keyword, int pageNo, int pageSize, String sortBy);

    List<ArticleResponse> getArticleByCategory(int pageNo, int pageSize, String sortBy, long categoryId);

    ArticleResponse createArticle(ArticleRequest articleRequest);

    ArticleResponse updateArticle(long id, ArticleRequest articleRequest);

    String hideArticle(long id);

    String showArticle(long id);

    void delete(long id);

    void deleteImage(long articleImageId);

}
