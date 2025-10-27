package edu.api.products.application.services.metrics;

import edu.api.products.application.dto.GeneralMetricsReport;
import edu.api.products.application.dto.ProductMetricSummary;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.domain.metric.ProductMetric;
import edu.api.products.infrastructure.metrics.ProductMetricRepository;
import edu.api.products.infrastructure.product.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class ProductMetricServiceImpl implements ProductMetricService {
    private final ProductMetricRepository productMetricRepository;
    private final ProductRepository productRepository;

    public ProductMetricServiceImpl(ProductMetricRepository productMetricRepository, ProductRepository productRepository) {
        this.productMetricRepository = productMetricRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductMetric getProductMetric(UUID productId) {
        if (productId == null) {
            throw new BusinessException("Product ID must not be null.");
        }
        return productMetricRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product metric not found."));
    }

    @Override
    @Transactional
    public void incrementClickMetric(UUID productId) {
        ProductMetric metric = getOrCreateMetric(productId);
        metric.setClicks(metric.getClicks() + 1);
        productMetricRepository.save(metric);
    }

    @Override
    @Transactional
    public void incrementArViewMetric(UUID productId) {
        ProductMetric metric = getOrCreateMetric(productId);
        metric.setArViews(metric.getArViews() + 1);
        productMetricRepository.save(metric);
    }

    @Override
    @Transactional
    public void incrementSearchAppearMetric(UUID productId) {
        ProductMetric metric = getOrCreateMetric(productId);
        metric.setSearchAppearances(metric.getSearchAppearances() + 1);
        productMetricRepository.save(metric);
    }

    @Override
    @Transactional
    public void incrementFavoritesAdds(UUID productId) {
        ProductMetric metric = getOrCreateMetric(productId);
        metric.setFavoritesAdds(metric.getFavoritesAdds() + 1);
        productMetricRepository.save(metric);
    }

    @Override
    @Transactional
    public void registerTimeOnPage(UUID productId, float durationSeconds) {
        ProductMetric metric = getOrCreateMetric(productId);
        float newTotal = (metric.getTimeSpentOnProductPageSeconds() + durationSeconds)/2;
        metric.setTimeSpentOnProductPageSeconds(newTotal);
        productMetricRepository.save(metric);
    }

    @Override
    @Transactional
    public void registerTimeInAr(UUID productId, float durationSeconds) {
        ProductMetric metric = getOrCreateMetric(productId);
        float newTotal = (metric.getTimeSpentOnArViewSeconds() + durationSeconds)/2;
        metric.setTimeSpentOnArViewSeconds(newTotal);
        productMetricRepository.save(metric);
    }

    private ProductMetric getOrCreateMetric(UUID productId) {
        if (productId == null) {
            throw new BusinessException("Product ID must not be null.");
        }

        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found to register metric."));

        return productMetricRepository.findByProductId(productId)
                .orElseGet(() -> ProductMetric.builder()
                        .productId(productId)
                        .clicks(0)
                        .arViews(0)
                        .searchAppearances(0)
                        .favoritesAdds(0)
                        .timeSpentOnProductPageSeconds(0)
                        .timeSpentOnArViewSeconds(0)
                        .rawScore(0.0)
                        .normalizedScore(0.0)
                        .build()
                );
    }

    @Override
    public GeneralMetricsReport getTenantMetricsReport(UUID tenantId) {
        int limit = 5;

        List<ProductMetricSummary> mostClicked = productMetricRepository.findMostClickedProductsByTenant(tenantId, PageRequest.of(0, limit));
        List<ProductMetricSummary> mostViewedAr = productMetricRepository.findMostViewedInArByTenant(tenantId, PageRequest.of(0, limit));
        List<ProductMetricSummary> mostSearched = productMetricRepository.findMostSearchedByTenant(tenantId, PageRequest.of(0, limit));

/*
        List<ProductMetricSummary> mostFavorited = productMetricRepository.findMostFavoritedByTenant(tenantId, PageRequest.of(0, limit));
*/

        Object[] row = (Object[]) productMetricRepository.getTotalAggregatesByTenant(tenantId)[0];

        long totalClicks = row[0] != null ? (long) row[0] : 0;
        long totalArViews = row[1] != null ? (long) row[1] : 0;
        long totalSearches = row[2] != null ? (long) row[2] : 0;
        long totalFavorites = row[3] != null ? (long) row[3] : 0;


        int totalProducts = productRepository.countByTenantIdAndStatus_DeletedFalse(tenantId);

        return GeneralMetricsReport.builder()
                .tenantId(tenantId)
                .period(YearMonth.now())
                .totalClicks((int) totalClicks)
                .totalArViews((int) totalArViews)
                .totalSearchAppearances((int) totalSearches)
                .totalFavoriteAdds((int) totalFavorites)
                .totalProducts(totalProducts)
                .mostClickedProducts(mostClicked)
                .mostViewedInAr(mostViewedAr)
                .mostSearchedProducts(mostSearched)
/*
                .mostFavoritedProducts(mostFavorited) // Nuevo campo
*/
                .build();
    }
}