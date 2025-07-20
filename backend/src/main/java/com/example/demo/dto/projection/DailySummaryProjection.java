package com.example.demo.dto.projection;

import java.util.Date;

public interface DailySummaryProjection {
    Date getDate();
    Integer getIncome();
    Integer getExpense();
}