package edu.api.products.application.dto;

import edu.api.products.domain.metric.TimeMetricType;

public record RegisterTimeMetricDTO (
        TimeMetricType metric,
        float duration
) {}
