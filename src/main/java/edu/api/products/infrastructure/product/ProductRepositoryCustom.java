package edu.api.products.infrastructure.product;

import edu.api.products.application.dto.ProductSearchCriteria;
import edu.api.products.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> search(ProductSearchCriteria productSearchCriteria, Pageable pageable);
}
