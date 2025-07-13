package com.example.demo.service;

import com.example.demo.dto.SummaryDTO;

import java.util.Date;

public interface SummaryService {
    SummaryDTO getSummary(Long userId, Date from, Date to, Long accountId);
}
