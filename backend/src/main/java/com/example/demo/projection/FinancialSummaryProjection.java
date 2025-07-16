package com.example.demo.projection;

public interface FinancialSummaryProjection {
    Integer getIncome();
    Integer getExpense();
    Integer getRemaining();
}