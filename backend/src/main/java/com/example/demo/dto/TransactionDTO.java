package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TransactionDTO {
    private Long id;
    private Integer amount;
    private String payee;
    private String notes;
    private Date date;
    private Long accountId;
    private Long categoryId;
    private String account; // Add account name
    private String category; // Add category name
} 