package edu.api.products.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralMetricsReport {

    private UUID tenantId;
    private YearMonth period;

    private int totalClicks;
    private int totalArViews;
    private int totalSearchAppearances;
    private int totalProducts;

    private List<ProductMetricSummary> mostClickedProducts;
    private List<ProductMetricSummary> mostViewedInAr;
    private List<ProductMetricSummary> mostSearchedProducts;

    private List<MonthlyGeneralMetric> monthlyMetrics;
}
