package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.dto.projection.CategorySummaryProjection;
import com.example.demo.dto.projection.DailySummaryProjection;
import com.example.demo.dto.projection.FinancialSummaryProjection;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {
    private final TransactionRepository transactionRepository;

    @Override
    public SummaryDTO getSummary(Long userId, Date from, Date to, Long accountId) {
        // Calculate period length for comparison
        LocalDate startDate = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long periodLength = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        Date lastPeriodStart = Date.from(startDate.minusDays(periodLength).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date lastPeriodEnd = Date.from(endDate.minusDays(periodLength).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Use optimized SQL queries instead of Java processing
        FinancialSummaryProjection currentPeriod = transactionRepository.getFinancialSummary(userId, from, to, accountId);
        FinancialSummaryProjection lastPeriod = transactionRepository.getFinancialSummary(userId, lastPeriodStart, lastPeriodEnd, accountId);

        // Get category summary
        List<CategorySummaryProjection> categoryData = transactionRepository.getEachCategoryExpenses(userId, from, to, accountId);
        List<SummaryDTO.CategorySummaryDTO> categories = processCategoryData(categoryData);

        // Get a daily summary
        List<DailySummaryProjection> dailyData = transactionRepository.getDailyIncomeAndExpense(userId, from, to, accountId);
        List<SummaryDTO.DaySummaryDTO> days = fillMissingDays(dailyData, startDate, endDate);

        return SummaryDTO.builder()
                .remainingAmount(safeValue(currentPeriod.getRemaining()))
                .remainingChange(calculatePercentageChange(
                        safeValue(currentPeriod.getRemaining()),
                        safeValue(lastPeriod.getRemaining())))
                .incomeAmount(safeValue(currentPeriod.getIncome()))
                .incomeChange(calculatePercentageChange(
                        safeValue(currentPeriod.getIncome()),
                        safeValue(lastPeriod.getIncome())))
                .expenseAmount(safeValue(currentPeriod.getExpense()))
                .expenseChange(calculatePercentageChange(
                        safeValue(currentPeriod.getExpense()),
                        safeValue(lastPeriod.getExpense())))
                .categories(categories)
                .days(days)
                .build();
    }

    private List<SummaryDTO.CategorySummaryDTO> processCategoryData(List<CategorySummaryProjection> categoryData) {
        List<SummaryDTO.CategorySummaryDTO> categories = categoryData.stream()
                .map(c -> {
                    SummaryDTO.CategorySummaryDTO category = new SummaryDTO.CategorySummaryDTO();
                    category.setName(c.getName());
                    category.setValue(c.getValue() != null ? c.getValue() : 0);
                    return category;
                })
                .collect(Collectors.toList());

        // Keep the top 3 categories and group others
        if (categories.size() > 3) {
            List<SummaryDTO.CategorySummaryDTO> topCategories = categories.subList(0, 3);
            int otherSum = categories.subList(3, categories.size()).stream()
                    .mapToInt(SummaryDTO.CategorySummaryDTO::getValue)
                    .sum();

            if (otherSum > 0) {
                SummaryDTO.CategorySummaryDTO other = new SummaryDTO.CategorySummaryDTO();
                other.setName("Other");
                other.setValue(otherSum);
                topCategories.add(other);
            }

            return topCategories;
        }

        return categories;
    }

    private List<SummaryDTO.DaySummaryDTO> fillMissingDays(List<DailySummaryProjection> dailyData, LocalDate startDate, LocalDate endDate) {
        // Convert projections to map for quick lookup
        Map<LocalDate, DailySummaryProjection> dataMap = dailyData.stream()
                .collect(Collectors.toMap(
                        d -> d.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        d -> d
                ));

        List<SummaryDTO.DaySummaryDTO> days = new ArrayList<>();
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            DailySummaryProjection dayData = dataMap.get(current);
            SummaryDTO.DaySummaryDTO day = new SummaryDTO.DaySummaryDTO();
            day.setDate(current.format(DateTimeFormatter.ISO_LOCAL_DATE));
            day.setIncome(dayData != null && dayData.getIncome() != null ? dayData.getIncome() : 0);
            day.setExpense(dayData != null && dayData.getExpense() != null ? dayData.getExpense() : 0);
            days.add(day);

            current = current.plusDays(1);
        }

        return days;
    }

    private double calculatePercentageChange(int current, int previous) {
        if (previous == 0) {
            return current == 0 ? 0.0 : 100.0;
        }
        return ((double) (current - previous) / Math.abs(previous)) * 100.0;
    }
    private int safeValue(Integer value) {
        return value != null ? value : 0;
    }

}