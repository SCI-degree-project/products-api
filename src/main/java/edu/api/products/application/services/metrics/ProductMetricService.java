package edu.api.products.application.services.metrics;

import edu.api.products.application.dto.GeneralMetricsReport;
import edu.api.products.application.dto.ProductMetricSummary;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.domain.Product;
import edu.api.products.domain.ProductMetric;
import edu.api.products.infrastructure.metrics.IProductMetricRepository;
import edu.api.products.infrastructure.product.IProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class ProductMetricService implements IProductMetricService {
    private final IProductMetricRepository productMetricRepository;
    private final IProductRepository productRepository;

    public ProductMetricService(IProductMetricRepository productMetricRepository, IProductRepository productRepository) {
        this.productMetricRepository = productMetricRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductMetric getProductMetric(UUID productId) {
        if (productId == null) {
            throw new BusinessException("Product ID must not be null.");
        }

        return productMetricRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
    }

    @Override
    public void incrementClickMetric(UUID productId) {
        if (productId == null) {
            throw new BusinessException("Product ID must not be null.");
        }

        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        ProductMetric metric = productMetricRepository.findByProductId(productId)
                .orElseGet(() -> ProductMetric.builder()
                        .productId(productId)
                        .clicks(0)
                        .build()
                );

        metric.setClicks(metric.getClicks() + 1);
        productMetricRepository.save(metric);
    }

    @Override
    public void incrementArViewMetric(UUID productId) {
        if (productId == null) {
            throw new BusinessException("Product ID must not be null.");
        }

        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        ProductMetric metric = productMetricRepository.findByProductId(productId)
                .orElseGet(() -> ProductMetric.builder()
                        .productId(productId)
                        .arViews(0)
                        .build()
                );

        metric.setArViews(metric.getArViews() + 1);
        productMetricRepository.save(metric);
    }

    @Override
    public void incrementSearchAppearMetric(UUID productId) {
        if (productId == null) {
            throw new BusinessException("Product ID must not be null.");
        }

        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        ProductMetric metric = productMetricRepository.findByProductId(productId)
                .orElseGet(() -> ProductMetric.builder()
                        .productId(productId)
                        .searchAppearances(0)
                        .build()
                );

        metric.setSearchAppearances(metric.getSearchAppearances() + 1);
        productMetricRepository.save(metric);
    }

    public GeneralMetricsReport getTenantMetricsReport(UUID tenantId) {
        int limit = 5;

        List<ProductMetricSummary> mostClicked = productMetricRepository.findMostClickedProductsByTenant(tenantId, PageRequest.of(0, limit));
        List<ProductMetricSummary> mostViewedAr = productMetricRepository.findMostViewedInArByTenant(tenantId, PageRequest.of(0, limit));
        List<ProductMetricSummary> mostSearched = productMetricRepository.findMostSearchedByTenant(tenantId, PageRequest.of(0, limit));

        Object[] row = (Object[]) productMetricRepository.getTotalAggregatesByTenant(tenantId)[0];
        long totalClicks = row[0] != null ? (long) row[0] : 0;
        long totalArViews = row[1] != null ? (long) row[1] : 0;
        long totalSearches = row[2] != null ? (long) row[2] : 0;


        int totalProducts = productRepository.countByTenantIdAndDeletedFalse(tenantId);

        return GeneralMetricsReport.builder()
                .tenantId(tenantId)
                .period(YearMonth.now())
                .totalClicks((int) totalClicks)
                .totalArViews((int) totalArViews)
                .totalSearchAppearances((int) totalSearches)
                .totalProducts(totalProducts)
                .mostClickedProducts(mostClicked)
                .mostViewedInAr(mostViewedAr)
                .mostSearchedProducts(mostSearched)
                .build();
    }
}
