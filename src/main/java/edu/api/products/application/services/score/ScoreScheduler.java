package edu.api.products.application.services.score;

import edu.api.products.domain.metric.ProductMetric;
import edu.api.products.infrastructure.metrics.ProductMetricRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScoreScheduler {
    private final ProductMetricRepository productMetricRepository;
    private final ScoreService scoreService;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void dailyScoreCalculation() {
        List<ProductMetric> productMetrics = productMetricRepository.findAll();

        if(productMetrics.isEmpty()) {
            return;
        }

        productMetrics.forEach(scoreService::calculateScore);

        double minRawScore = productMetrics.stream()
                .mapToDouble(ProductMetric::getRawScore)
                .min().orElse(0.0);

        double maxRawScore = productMetrics.stream()
                .mapToDouble(ProductMetric::getRawScore)
                .max().orElse(1.0);

        for(ProductMetric metric: productMetrics) {
            double normalizeScore = scoreService.normalizeScore(
                    metric.getRawScore(),
                    minRawScore,
                    maxRawScore);
            metric.setNormalizedScore(normalizeScore);
        }

        productMetricRepository.saveAll(productMetrics);
    }
}
