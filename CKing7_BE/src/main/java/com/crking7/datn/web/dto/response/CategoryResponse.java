package com.crking7.datn.web.dto.response;
import com.crking7.datn.models.Banner;
import com.crking7.datn.models.Category;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CategoryResponse {
	private long id;

	private String title;

	private  int status;

	private String description;

	private String urlImage;

	private int type;

	private Set<Banner> banners = new HashSet<>();

	private List<CategoryResponse> childCategories = new ArrayList<>();

	public void setParentCategory(Category parentCategory) {
		// Không làm gì trong phương thức này
	}
}
