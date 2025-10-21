package edu.api.products.application.services.score;

import edu.api.products.domain.metric.ProductMetric;
import org.springframework.stereotype.Service;

@Service
public class ScoreServiceImpl implements ScoreService {
    private final static double CLICK_W = 0.20;
    private final static double AR_VIEW_W = 0.30;
    private final static double SEARCH_APPEAR_W = 0.10;
    private final static double FAVORITE_ADD_W = 0.20;

    @Override
    public void calculateScore(ProductMetric metric) {
        double rawScore = (metric.getClicks() * CLICK_W) +
                (metric.getArViews() * AR_VIEW_W) +
                (metric.getSearchAppearances() * SEARCH_APPEAR_W) +
                (metric.getFavoritesAdds() * FAVORITE_ADD_W);

        metric.setRawScore(rawScore);
    }

    @Override
    public double normalizeScore(double rawScore, double minRawScore, double maxRawScore) {
        if (maxRawScore == minRawScore) return 1.0;
        return (rawScore - minRawScore) / (maxRawScore - minRawScore);
    }
}
