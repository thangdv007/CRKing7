package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getCategories(int pageNo, int pageSize, String sortBy);
    List<CategoryResponse> getAllCategory(int pageNo, int pageSize, String sortBy);
    List<CategoryResponse> getCategoriesByType(int type);
    CategoryResponse getCategoryById (long id);
    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(long id, CategoryRequest categoryRequest);

    CategoryResponse hideCategory(long id);

    void deleteCategory(long id);

}
