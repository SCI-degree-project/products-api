package edu.api.products.application.services;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.domain.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface IProductService {
    void create(ProductDTO product);
    void get(UUID id);
    void update(ProductDTO product);
    void delete(UUID id);
}
