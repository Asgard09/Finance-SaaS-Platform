package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DailySummaryDTO {
    private Date date;
    private Integer income;
    private Integer expense;
} 