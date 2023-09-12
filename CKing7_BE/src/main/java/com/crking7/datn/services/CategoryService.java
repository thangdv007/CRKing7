package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getCategories(int pageNo, int pageSize, String sortBy);
    CategoryResponse getCategoryByName(String title);
    List<CategoryResponse> getAllCategory(String keyword, int pageNo, int pageSize, String sortBy);
    List<CategoryResponse> getCategoriesByType(int type);
    List<CategoryResponse> getCategoriesByParentCategory(Long parentId);
    CategoryResponse getCategoryById (long id);
    List<CategoryResponse> getParentCategory();
    CategoryResponse getCategoryAdmin (long id);
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategory(long id, CategoryRequest categoryRequest);
    String hideCategory(long id);
    String showCategory(long id);
    void deleteCategory(long id);

}
