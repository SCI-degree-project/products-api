package edu.api.products.application.services;

import edu.api.products.application.dto.ProductDTO;

import java.util.UUID;

public interface IProductService {
    void create(ProductDTO product);
    void get(UUID id);
    void update(UUID productId, ProductDTO product);
    void delete(UUID id);
}
