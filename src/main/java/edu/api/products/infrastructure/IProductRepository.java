package edu.api.products.infrastructure;

import edu.api.products.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByTenantId(UUID tenantId, Pageable pageable);
    void deleteByTenantId(UUID tenantId);
}
