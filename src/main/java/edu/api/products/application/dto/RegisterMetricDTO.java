package edu.api.products.application.dto;

import edu.api.products.domain.metric.MetricType;

public record RegisterMetricDTO (
        MetricType metric
) {}
