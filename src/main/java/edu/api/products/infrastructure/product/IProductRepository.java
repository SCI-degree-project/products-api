package edu.api.products.infrastructure.product;

import edu.api.products.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByTenantId(UUID tenantId, Pageable pageable);
    void deleteByTenantId(UUID tenantId);
    List<Product> findAllByIdIn(List<UUID> ids);

    Page<Product> findAllByTenantIdAndIsDeletedFalse(UUID tenantId, Pageable pageable);
    Optional<Product> findByIdAndIsDeletedFalse(UUID productId);
    List<Product> findAllByIdInAndIsDeletedFalse(List<UUID> ids);
}
