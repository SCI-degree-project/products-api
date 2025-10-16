package edu.api.products.application.services.metrics;

import edu.api.products.domain.ProductMetric;

import java.util.UUID;

public interface ProductMetricService {
    ProductMetric getProductMetric(UUID productId);
    void incrementClickMetric(UUID productId);
    void incrementArViewMetric(UUID productId);
    void incrementSearchAppearMetric(UUID productId);
}
