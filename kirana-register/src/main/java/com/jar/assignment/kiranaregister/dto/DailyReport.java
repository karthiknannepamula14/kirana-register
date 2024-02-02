package com.jar.assignment.kiranaregister.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class DailyReport {
    private LocalDate date;
    private int totalTransactions;
    private double totalAmountInINR;
    private double averageAmountInINR;
}
