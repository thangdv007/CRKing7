package com.crking7.datn.services.impl;

import com.crking7.datn.config.Constants;
import com.crking7.datn.models.*;
import com.crking7.datn.repositories.ArticleImageRepository;
import com.crking7.datn.repositories.ArticleRepository;
import com.crking7.datn.repositories.CategoryRepository;
import com.crking7.datn.services.ArticleService;
import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.mapper.ArticleMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              ArticleImageRepository articleImageRepository, CategoryRepository categoryRepository,
                              ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleImageRepository = articleImageRepository;
        this.categoryRepository = categoryRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public ArticleResponse getArticle(long articleId) {
        Article article = articleRepository.findByIdAndStatus(articleId, Constants.ACTIVE_STATUS);
        if (article != null) {
            return articleMapper.mapToResponse(article);
        }
        return null;
    }

    @Override
    public List<ArticleResponse> getArticles(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<Article> articles = articleRepository.findAllByStatus(pageable, Constants.ACTIVE_STATUS);

        return articles.getContent().stream()
                .map(articleMapper::mapToResponse)
                .toList();
    }

    @Override
    public List<ArticleResponse> getArticleByCategory(int pageNo, int pageSize, String sortBy, long categoryId) {
        Category category = categoryRepository.findByStatusAndIdAndType(Constants.ACTIVE_STATUS, categoryId, Constants.ARTICLE_TYPE);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<Article> articles = articleRepository.findAllByCategoryAndStatus(pageable, category, Constants.ACTIVE_STATUS);

        if (!articles.isEmpty()) {
            return articles.getContent().stream()
                    .map(articleMapper::mapToResponse)
                    .toList();
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public ArticleResponse createArticle(ArticleRequest articleRequest) {
        Article article = articleRepository.findByTitle(articleRequest.getTitle());
        if (article == null) {
            Article articleNew = articleMapper.mapRequestToModel(articleRequest);
            Date date = new Date();
            articleNew.setCreatedDate(date);
            articleNew.setModifiedDate(date);
            articleNew.setStatus(1);
            Article articleResp = articleRepository.save(articleNew);

            long articleId = articleNew.getId();

            for (ArticleImageRequest a : articleRequest.getImages()) {
                ArticleImage image = new ArticleImage();
                image.setArticle(articleResp);
                image.setUrl(a.getUrl());
                articleImageRepository.save(image);
            }
            Article article1 = articleRepository.findById(articleId).orElseThrow();
            List<ArticleImage> articleImages = articleImageRepository.findAllByArticle(article1);
            article1.setArticleImages(articleImages);

            return articleMapper.mapToResponse(article1);
        } else {
            return null;
        }
    }

    @Override
    public ArticleResponse updateArticle(long id, ArticleUDRequest articleRequest) {
        Article article = articleRepository.findById(id).orElseThrow();
        List<ArticleImageUDRequest> imageRequests = articleRequest.getImages();
        if (article == null) {
            return null;
        }
        articleMapper.updateModel(article, articleRequest);
        Date currentDate = new Date();
        article.setModifiedDate(currentDate);
        articleRepository.save(article);
        if (imageRequests != null) {
            List<ArticleImage> existingImages = articleImageRepository.findAllByArticle(article);
            List<ArticleImage> newImages = new ArrayList<>();
            for (ArticleImageUDRequest imageRequest : imageRequests) {
                if (imageRequest.getId() == 0) {
                    // Nếu hình ảnh không tồn tại, tạo hình ảnh mới và liên kết nó với sản phẩm.
                    ArticleImage newImage = new ArticleImage();
                    newImage.setUrl(imageRequest.getUrl());
                    newImage.setArticle(article);
                    ArticleImage image = articleImageRepository.save(newImage);
                    newImages.add(image);
                } else {
                    ArticleImage existingImage = articleImageRepository.findById(imageRequest.getId()).orElseThrow();
                    // Nếu hình ảnh đã tồn tại, cập nhật các thuộc tính của nó.
                    existingImage.setUrl(imageRequest.getUrl());
                }
            }
            existingImages.addAll(newImages);
        }

        List<ArticleImage> articleImages = articleImageRepository.findAllByArticle(article);
        article.setArticleImages(articleImages);
        return articleMapper.mapToResponse(article);
    }

    @Override
    public ArticleResponse hideArticle(long id) {
        Article article = articleRepository.findByIdAndStatus(id, Constants.ACTIVE_STATUS);
        if (article == null) {
            return null;
        }
        Date currentDate = new Date();
        article.setModifiedDate(currentDate);
        article.setStatus(0);
        articleRepository.save(article);
        return articleMapper.mapToResponse(article);
    }

    @Override
    public ArticleResponse showArticle(long id) {
        Article article = articleRepository.findByIdAndStatus(id, Constants.INACTIVE_STATUS);
        if (article == null) {
            return null;
        }
        Date currentDate = new Date();
        article.setModifiedDate(currentDate);
        article.setStatus(1);
        articleRepository.save(article);
        return articleMapper.mapToResponse(article);
    }

    @Override
    public void delete(long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        if (article != null) {
            article.setCategory(null);
            articleRepository.save(article);
        }
        articleRepository.delete(article);
    }

    @Override
    public void deleteImage(long articleImageId) {
        ArticleImage articleImage = articleImageRepository.findById(articleImageId).orElseThrow();
        if (articleImage != null) {
            articleImage.setArticle(null);
            articleImageRepository.save(articleImage);
        }
        articleImageRepository.delete(articleImage);
    }
}
