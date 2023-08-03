package com.crking7.datn.web.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ArticleRequest {
    private String title;
    private String titleSummary;
    private String content;
    private String tag;
    private long userId;
    private long categoryId;
    private List<ArticleImageRequest> images;
}
