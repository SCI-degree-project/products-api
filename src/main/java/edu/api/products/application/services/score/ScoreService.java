package edu.api.products.application.services.score;

import edu.api.products.domain.metric.ProductMetric;

public interface ScoreService {
    void calculateScore(ProductMetric metric);
    double normalizeScore(double rawScore, double minRawScore, double maxRawScore);
}
