package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinancialSummaryDTO {
    private Integer income;
    private Integer expense;
    private Integer remaining;
} 