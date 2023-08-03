package com.crking7.datn.mapper;

import com.crking7.datn.models.Article;
import com.crking7.datn.models.ArticleImage;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.response.ArticleImageResponse;
import com.crking7.datn.web.dto.response.ArticleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ArticleMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "images", source = "articleImages")
    ArticleResponse mapToResponse(Article article);

    ArticleImageResponse mapImageToResponse(ArticleImage articleImage);

    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "user.id", source = "userId")
    Article mapRequestToModel(ArticleRequest articleRequest);
}
