package com.crking7.datn.web.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryRequest {
	private String title;

	private int type;
	
	private Date modifiedDate;

	private Date createdDate;
	 
	private String description;

	private String urlImage;

	private long parentCategoryId;
}
