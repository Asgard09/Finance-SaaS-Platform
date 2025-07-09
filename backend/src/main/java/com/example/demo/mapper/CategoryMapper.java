package com.example.demo.mapper;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "user.id", target = "userId")
    CategoryDTO toDTO(Category category);
    List<CategoryDTO> toDtoList(List<Category> categories);
    Category toEntity(CategoryDTO categoryDTO);
}
