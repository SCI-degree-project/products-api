package edu.api.products.infrastructure.product;

import edu.api.products.domain.product.Material;
import edu.api.products.domain.product.Product;
import edu.api.products.domain.product.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByTenantId(UUID tenantId, Pageable pageable);
    void deleteByTenantId(UUID tenantId);
    List<Product> findAllByIdIn(List<UUID> ids);

    Page<Product> findAllByTenantIdAndStatus_DeletedFalse(UUID tenantId, Pageable pageable);
    Optional<Product> findByIdAndStatus_DeletedFalse(UUID productId);
    List<Product> findAllByIdInAndStatus_DeletedFalse(List<UUID> ids);
    int countByTenantIdAndStatus_DeletedFalse(UUID tenantId);


    @Query(value = """
        SELECT p
        FROM Product p
        LEFT JOIN ProductMetric m ON p.id = m.productId
        WHERE p.tenantId = :tenantId
          AND :material MEMBER OF p.materials
          AND p.status.deleted = FALSE
    """)
    Page<Product> findAllByTenantIdAndMaterial(
            @Param("tenantId") UUID tenantId,
            @Param("material") Material material,
            Pageable pageable
    );

    @Query(value = """
        SELECT p
        FROM Product p
        LEFT JOIN ProductMetric m ON p.id = m.productId
        WHERE p.tenantId = :tenantId
          AND p.style = :style
          AND p.status.deleted = FALSE
    """)
    Page<Product> findAllByTenantIdAndStyle(
            @Param("tenantId") UUID tenantId,
            @Param("style") Style style,
            Pageable pageable
    );

    @Query(value = """
    SELECT p.*
    FROM products p
    LEFT JOIN product_metrics m ON p.product_id = m.product_id
    WHERE m.normalized_score >= 0.4
      AND p.deleted = FALSE
      AND p.tenant_id IN (
          SELECT DISTINCT tenant_id
          FROM products
          GROUP BY tenant_id
          LIMIT 20
      )
    """, nativeQuery = true)
    Page<Product> findAllByScore(
            Pageable pageable
    );
}
