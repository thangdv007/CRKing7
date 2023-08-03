package com.crking7.datn.web.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ArticleResponse {
    private long id;
    private String title;
    private String titleSummary;
    private String content;
    private String tag;
    private String createdDate;
    private long userId;
    private long categoryId;
    private List<ArticleImageResponse> images;
}
