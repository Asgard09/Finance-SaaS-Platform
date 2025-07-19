package com.example.demo.projection;

import java.util.Date;

public interface DailySummaryProjection {
    Date getDate();
    Integer getIncome();
    Integer getExpense();
}