package com.example.demo.dto.projection;

public interface FinancialSummaryProjection {
    Integer getIncome();
    Integer getExpense();
    Integer getRemaining();
}