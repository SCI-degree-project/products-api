package edu.api.products.application.services;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductSearchCriteria;
import edu.api.products.application.dto.UpdateProductDTO;
import edu.api.products.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    Product create(ProductDTO product);
    Product getById(UUID id);
    Product update(UUID tenantId, UUID productId, ProductDTO product);
    void delete(UUID tenantId, UUID id);
    Page<Product> getProducts(UUID tenantId, Pageable pageable);
    void deleteAllByTenantId(UUID tenantId);
    List<Product> getProductsByIds(List<UUID> productIds);
    Product partialUpdate(UUID tenantId, UUID productId, UpdateProductDTO dto);
    Page<Product> search(ProductSearchCriteria criteria, Pageable pageable);
}
