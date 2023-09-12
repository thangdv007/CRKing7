package com.crking7.datn.services.impl;

import com.crking7.datn.config.Constants;
import com.crking7.datn.models.Category;
import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.CategoryResponse;
import com.crking7.datn.exceptions.ResourceNotFoundException;
import com.crking7.datn.mapper.CategoryMapper;
import com.crking7.datn.repositories.CategoryRepository;
import com.crking7.datn.services.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public List<CategoryResponse> getCategories(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Category> categories = categoryRepository.findAllByStatus(pageable, Constants.ACTIVE_STATUS);
        if (!categories.isEmpty()) {
            return categories.stream()
                    .map(categoryMapper::mapModelToResponse)
                    .toList();
        } else {
            return null;
        }
    }

    @Override
    public List<CategoryResponse> getAllCategory(String keyword, int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Category> categories = categoryRepository.getAllByKeyword(keyword, pageable);
        if (!categories.isEmpty()) {
            return categories.stream()
                    .map(categoryMapper::mapModelToResponse)
                    .toList();
        } else {
            return null;
        }
    }

    @Override
    public CategoryResponse getCategoryByName(String title) {
        Category category = categoryRepository.findByTitle(title);
        return categoryMapper.mapModelToResponse(category);
    }

    @Override
    public List<CategoryResponse> getCategoriesByType(int type) {
        List<Category> categories;
        if (type != -1) {
            categories = categoryRepository.findAllByStatusAndType(Constants.ACTIVE_STATUS, type);
        } else {
            categories = categoryRepository.findByStatus(Constants.ACTIVE_STATUS);
        }

        List<Category> parentCategories = new ArrayList<>();
        if (categories != null) {
            // Tìm tất cả các parent category
            for (Category category : categories) {
                if (category.getParentCategory() == null) {
                    parentCategories.add(category);
                }
            }

            // Chuyển đổi parent categories và child categories sang CategoryResponse
            List<CategoryResponse> categoryResponses = new ArrayList<>();
            for (Category parentCategory : parentCategories) {
                CategoryResponse parentCategoryResponse = categoryMapper.mapModelToResponse(parentCategory);

                // Tìm các child categories tương ứng với parent category
                List<CategoryResponse> childCategories = new ArrayList<>();
                for (Category category : categories) {
                    if (category.getParentCategory() != null && category.getParentCategory().getId() == parentCategory.getId()) {
                        CategoryResponse childCategoryResponse = categoryMapper.mapModelToResponse(category);
                        childCategories.add(childCategoryResponse);
                    }
                }

                parentCategoryResponse.setChildCategories(childCategories);
                categoryResponses.add(parentCategoryResponse);
            }

            return categoryResponses;
        }
        return null;
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = categoryRepository.findByTitle(categoryRequest.getTitle());
        if (category != null) {
            return null;
        } else {
            Category categoryNew = categoryMapper.mapRequestToModel(categoryRequest);
            Category parentCategory = null;
            if (categoryRequest.getParentCategoryId() != 0) {
                parentCategory = categoryRepository.findById(categoryRequest.getParentCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryRequest.getParentCategoryId()));
                categoryNew.setParentCategory(parentCategory);
            }else{
                categoryNew.setParentCategory(null);
            }
            Date date = new Date();
            categoryNew.setCreatedDate(date);
            categoryNew.setModifiedDate(date);
            categoryNew.setStatus(1);
            categoryNew.setType(categoryRequest.getType());

            Category categoryResp = categoryRepository.save(categoryNew);
            return categoryMapper.mapModelToResponse(categoryResp);
        }
    }

    @Override
    public CategoryResponse getCategoryAdmin(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        if (category != null) {
            return categoryMapper.mapModelToResponse(category);
        } else {
            return null;
        }
    }

    @Override
    public CategoryResponse getCategoryById(long id) {
        Category category = categoryRepository.findByIdAndStatus(id, Constants.ACTIVE_STATUS);
        if (category != null) {
            return categoryMapper.mapModelToResponse(category);
        } else {
            return null;
        }
    }

    @Override
    public List<CategoryResponse> getParentCategory() {
        List<Category> categories = categoryRepository.findAllWithNoParentCategory();
        if (!categories.isEmpty()) {
            return categories.stream()
                    .map(categoryMapper::mapModelToResponse)
                    .toList();
        } else {
            return null;
        }
    }

    @Override
    public List<CategoryResponse> getCategoriesByParentCategory(Long parentId) {
        List<Category> categories = categoryRepository.findByParentCategoryId(parentId);
        if (!categories.isEmpty()) {
            return categories.stream()
                    .map(categoryMapper::mapModelToResponse)
                    .toList();
        } else {
            return null;
        }
    }

    @Override
    public CategoryResponse updateCategory(long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        if (category == null) {
            return null; // Không tìm thấy danh mục
        }
        categoryMapper.updateModel(category, categoryRequest);
        Date currentDate = new Date();
        category.setModifiedDate(currentDate);
        if(categoryRequest.getParentCategoryId() != 0 ){
            Category parentCategory = categoryRepository.findById(categoryRequest.getParentCategoryId()).orElseThrow();
            category.setParentCategory(parentCategory);
        }else{
            category.setParentCategory(null);
        }
        categoryRepository.save(category);
        return categoryMapper.mapModelToResponse(category);
    }

    @Override
    public String hideCategory(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        if (category == null) {
            return null; // Không tìm thấy danh mục
        }
        Date currentDate = new Date();
        category.setModifiedDate(currentDate);
        category.setStatus(0);
        categoryRepository.save(category);
        return "Danh mục đã được ẩn";
    }

    @Override
    public String showCategory(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        if (category == null) {
            return null; // Không tìm thấy danh mục
        }
        Date currentDate = new Date();
        category.setModifiedDate(currentDate);
        if (category.getStatus() == 0) {
            category.setStatus(1);
        }
        categoryRepository.save(category);
        return "Danh mục đã được hiện";
    }

    @Override
    public void deleteCategory(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(category);
    }
}
