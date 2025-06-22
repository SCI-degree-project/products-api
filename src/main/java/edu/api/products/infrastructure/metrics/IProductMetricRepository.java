package edu.api.products.infrastructure.metrics;

import edu.api.products.domain.ProductMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IProductMetricRepository extends JpaRepository<ProductMetric, UUID> {
    Optional<ProductMetric> findByProductId(UUID productId);
}
