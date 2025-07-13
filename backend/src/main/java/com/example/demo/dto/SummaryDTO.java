package com.example.demo.dto;

import com.example.demo.entity.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class SummaryDTO {
    private int remainingAmount;
    private double remainingChange;
    private int incomeAmount;
    private double incomeChange;
    private int expenseAmount;
    private double expenseChange;
    private List<Category> categories;
    private List<Date> days;
}
