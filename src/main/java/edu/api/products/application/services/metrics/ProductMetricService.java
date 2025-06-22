package edu.api.products.application.services.metrics;

import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.domain.Product;
import edu.api.products.domain.ProductMetric;
import edu.api.products.infrastructure.metrics.IProductMetricRepository;
import edu.api.products.infrastructure.product.IProductRepository;
import org.springframework.stereotype.Service;

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
}
