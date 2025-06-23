package edu.api.products.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyGeneralMetric {
    private YearMonth month;
    private int totalClicks;
    private int totalArViews;
    private int totalSearchAppearances;
}

