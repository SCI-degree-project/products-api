package edu.api.products.application.services;

import edu.api.products.domain.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface IProductService {
    void create(Product product);
    void get(UUID id);
    void update(Product product);
    void delete(UUID id);
}
