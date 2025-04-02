package edu.api.products.application.services;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.domain.Product;

import java.util.UUID;

public interface IProductService {
    void create(ProductDTO product);
    Product get(UUID id);
    void update(UUID productId, ProductDTO product);
    void delete(UUID id);
}
