package edu.api.products.infrastructure.metrics;

import edu.api.products.application.dto.ProductMetricSummary;
import edu.api.products.domain.ProductMetric;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductMetricRepository extends JpaRepository<ProductMetric, UUID> {
    Optional<ProductMetric> findByProductId(UUID productId);
    @Query("""
        SELECT new edu.api.products.application.dto.ProductMetricSummary(
            p.id, p.name,
            COALESCE(m.clicks, 0),
            COALESCE(m.arViews, 0),
            COALESCE(m.searchAppearances, 0)
        )
        FROM ProductMetric m
        JOIN Product p ON m.productId = p.id
        WHERE p.tenantId = :tenantId
        ORDER BY m.clicks DESC
    """)
    List<ProductMetricSummary> findMostClickedProductsByTenant(@Param("tenantId") UUID tenantId, Pageable pageable);

    @Query("""
        SELECT new edu.api.products.application.dto.ProductMetricSummary(
            p.id, p.name,
            COALESCE(m.clicks, 0),
            COALESCE(m.arViews, 0),
            COALESCE(m.searchAppearances, 0)
        )
        FROM ProductMetric m
        JOIN Product p ON m.productId = p.id
        WHERE p.tenantId = :tenantId
        ORDER BY m.arViews DESC
    """)
    List<ProductMetricSummary> findMostViewedInArByTenant(@Param("tenantId") UUID tenantId, Pageable pageable);

    @Query("""
        SELECT new edu.api.products.application.dto.ProductMetricSummary(
            p.id, p.name,
            COALESCE(m.clicks, 0),
            COALESCE(m.arViews, 0),
            COALESCE(m.searchAppearances, 0)
        )
        FROM ProductMetric m
        JOIN Product p ON m.productId = p.id
        WHERE p.tenantId = :tenantId
        ORDER BY m.searchAppearances DESC
    """)
    List<ProductMetricSummary> findMostSearchedByTenant(@Param("tenantId") UUID tenantId, Pageable pageable);

    @Query("""
        SELECT SUM(m.clicks), SUM(m.arViews), SUM(m.searchAppearances)
        FROM ProductMetric m
        JOIN Product p ON m.productId = p.id
        WHERE p.tenantId = :tenantId
    """)
    Object[] getTotalAggregatesByTenant(@Param("tenantId") UUID tenantId);

}
