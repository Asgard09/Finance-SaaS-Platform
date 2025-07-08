package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private Long id;        // Add this field
    private Long plaiId;
    private Long userId;
    private String name;
}
