package com.crking7.datn.web.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ArticleUDRequest {
    private String title;
    private String titleSummary;
    private String content;
    private String tag;
    private long userId;
    private long categoryId;
    private List<ArticleImageUDRequest> images;
}
