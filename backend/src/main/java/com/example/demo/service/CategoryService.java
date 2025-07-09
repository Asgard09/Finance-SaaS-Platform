package com.example.demo.service;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDTO categoryDTO, User user);
    List<CategoryDTO> getAllCategory(Long userId);
    void deleteCategory(Long categoryId, Long userId);
    void deleteAllCategories(Long userId);
}
