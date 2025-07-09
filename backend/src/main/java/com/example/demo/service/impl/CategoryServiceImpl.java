package com.example.demo.service.impl;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category createCategory(CategoryDTO categoryDTO, User user) {
        Category category = categoryMapper.toEntity(categoryDTO);
        category.setUser(user);
        return categoryRepository.save(category);
    }

    @Override
    public List<CategoryDTO> getAllCategory(Long userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);
        return categoryMapper.toDtoList(categories);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId, Long userId) {
        int deletedCount = categoryRepository.deleteByIdAndUserId(categoryId, userId);
        if (deletedCount == 0) {
            throw new RuntimeException("Category not found or you don't have permission to delete this category");
        }
    }

    @Override
    @Transactional
    public void deleteAllCategories(Long userId) {
        categoryRepository.deleteByUserId(userId);
    }
}
