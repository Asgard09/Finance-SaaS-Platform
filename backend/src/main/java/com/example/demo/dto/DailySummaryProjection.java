package com.example.demo.dto;

import java.util.Date;

public interface DailySummaryProjection {
    Date getDate();
    Integer getIncome();
    Integer getExpense();
}