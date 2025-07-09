package com.example.demo.repository;

import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM categories c WHERE c.id = :categoryId AND c.user.id = :userId")
    int deleteByIdAndUserId(@Param("categoryId") Long categoryId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM categories c WHERE c.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
