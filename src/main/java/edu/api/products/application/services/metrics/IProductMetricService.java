package edu.api.products.application.services.metrics;

import edu.api.products.domain.ProductMetric;

import java.util.UUID;

public interface IProductMetricService {
    ProductMetric getProductMetric(UUID productId);
    void incrementClickMetric(UUID productId);
}
