package com.example.demo.service.impl;

import com.example.demo.dto.SummaryDTO;
import com.example.demo.service.SummaryService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class SummaryServiceImpl implements SummaryService {
    @Override
    public SummaryDTO getSummary(Long userId, Date from, Date to, Long accountId) {
        LocalDate startDate = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long periodLength = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return null;
    }
}
