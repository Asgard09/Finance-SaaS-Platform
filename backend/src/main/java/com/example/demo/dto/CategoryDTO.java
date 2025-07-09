package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private Long id;        
    private Long plaiId;
    private Long userId;
    private String name;
}
