package edu.api.products.application.services;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.mappers.ProductMapper;
import edu.api.products.domain.Product;
import edu.api.products.infrastructure.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements IProductService {
    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void create(ProductDTO product) {
        if (product == null) {
            throw new BusinessException("Product data must not be null.");
        }

        Product newProduct = ProductMapper.toEntity(product);

        validateProduct(newProduct);

        productRepository.save(newProduct);
    }

    @Override
    public void get(UUID id) {
        productRepository.findById(id);
    }

    @Override
    public void update(UUID productId, ProductDTO product) {
        if (productId == null) {
            throw new BusinessException("Product ID must not be null.");
        }
        if (product == null) {
            throw new BusinessException("Product data must not be null.");
        }

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        existingProduct.setName(product.getProductName());
        existingProduct.setDescription(product.getProductDescription());
        existingProduct.setPrice(product.getProductPrice());

        validateProduct(existingProduct);

        productRepository.save(existingProduct);
    }

    @Override
    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

    private void validateProduct(Product product) {
        if(product.getName() == null || product.getName().isEmpty()){
            throw new BusinessException("Unable to create the product. Empty name.");
        }

        double MAX_PRICE = 100000;
        if(product.getPrice() <= 0 || product.getPrice() >= MAX_PRICE){
            throw new BusinessException("Unable to create the product. Price out of range.");
        }
    }
}
