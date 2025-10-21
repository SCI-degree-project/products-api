package edu.api.products.application.services.metrics;

import edu.api.products.application.dto.GeneralMetricsReport;
import edu.api.products.domain.metric.ProductMetric;

import java.util.UUID;

public interface ProductMetricService {
    ProductMetric getProductMetric(UUID productId);
    void incrementClickMetric(UUID productId);
    void incrementArViewMetric(UUID productId);
    void incrementSearchAppearMetric(UUID productId);
    void incrementFavoritesAdds(UUID productId);
    void registerTimeOnPage(UUID productId, float durationSeconds);
    void registerTimeInAr(UUID productId, float durationSeconds);
    GeneralMetricsReport getTenantMetricsReport(UUID tenantId);
}
