package edu.api.products.application.services.product;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductSearchCriteria;
import edu.api.products.application.dto.UpdateProductDTO;
import edu.api.products.domain.product.Material;
import edu.api.products.domain.product.Product;
import edu.api.products.domain.product.Style;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product create(ProductDTO product);
    Product getById(UUID id);
    Product update(UUID tenantId, UUID productId, ProductDTO product);
    void delete(UUID tenantId, UUID id);
    Page<Product> getProducts(UUID tenantId, Pageable pageable);
    void deleteAllByTenantId(UUID tenantId);
    List<Product> getBatchProductsByIds(List<UUID> productIds);
    Product partialUpdate(UUID tenantId, UUID productId, UpdateProductDTO dto);
    Page<Product> search(ProductSearchCriteria criteria, Pageable pageable);
    Page<Product> getProductsByMaterial(UUID tenantId, Material material, Pageable pageable);
    Page<Product> getProductsByStyle(UUID tenantId, Style style, Pageable pageable);
}
