package com.example.demo.controller;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestBody CategoryDTO categoryDTO, @AuthenticationPrincipal OAuth2User principal){
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userService.findOrCreateUser((principal));
        Category saved = categoryService.createCategory(categoryDTO, user);
        return ResponseEntity.ok(categoryMapper.toDTO(saved));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CategoryDTO>> getAllCategoryOfCurrentUser(@AuthenticationPrincipal OAuth2User principal){
        if (principal == null) return ResponseEntity.status(401).build();
        User user = userService.findOrCreateUser(principal);
        return ResponseEntity.ok(categoryService.getAllCategory(user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            User user = userService.findOrCreateUser(principal);
            categoryService.deleteCategory(id, user.getId());
            return ResponseEntity.ok("Category deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllCategories(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        
        User user = userService.findOrCreateUser(principal);
        categoryService.deleteAllCategories(user.getId());
        return ResponseEntity.ok("All categories deleted successfully");
    }
}
