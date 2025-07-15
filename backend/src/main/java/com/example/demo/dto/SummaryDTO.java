package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryDTO {
    private int remainingAmount;
    private double remainingChange;
    private int incomeAmount;
    private double incomeChange;
    private int expenseAmount;
    private double expenseChange;
    private List<CategorySummaryDTO> categories;
    private List<DaySummaryDTO> days;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummaryDTO {
        private String name;
        private Integer value;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DaySummaryDTO {
        private String date;
        private Integer income;
        private Integer expense;
    }
}
