package com.crking7.datn.web.dto.response;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ProductResponse {
    private Long id;

    private String name;

    private String description;

    private String sku;

    private String material;

    private int price;

    private int salePrice;

    private Date modifiedDate;

    private Date createdDate;

    private int status;

    private long author;

    private long category;

    private List<ColorResponse> colors;

    private List<ProductImageResponse> images;
}
