package edu.api.products.application.services;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.InvalidTenantException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.mappers.ProductMapper;
import edu.api.products.domain.Product;
import edu.api.products.infrastructure.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService implements IProductService {
    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(ProductDTO product) {

        if (product == null) {
            throw new BusinessException("Product data must not be null.");
        }

        Product newProduct = ProductMapper.toEntity(product);

        validateProduct(newProduct);

        return productRepository.save(newProduct);
    }

    @Override
    public Product get(UUID tenantId, UUID id) {
        if (tenantId == null) {
            throw new BusinessException("Tenant Id must not be null.");
        }

        if (id == null) {
            throw new BusinessException("Product Id must not be null.");
        }

        return existProduct(tenantId, id);
    }

    @Override
    public Product update(UUID tenantId, UUID productId, ProductDTO product) {
        if (tenantId == null) {
            throw new BusinessException("Tenant Id must not be null.");
        }

        if (productId == null) {
            throw new BusinessException("Product Id must not be null.");
        }

        if (product == null) {
            throw new BusinessException("Product data must not be null.");
        }

        Product existingProduct = existProduct(tenantId, productId);

        existingProduct.setName(product.name());
        existingProduct.setDescription(product.description());
        existingProduct.setPrice(product.price());

        validateProduct(existingProduct);

        return productRepository.save(existingProduct);
    }

    @Override
    public void delete(UUID tenantId, UUID id) {
        if (tenantId == null) {
            throw new BusinessException("Tenant Id must not be null.");
        }

        if (id == null) {
            throw new BusinessException("Product ID must not be null.");
        }

        existProduct(tenantId, id);

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

    private Product existProduct(UUID tenantId, UUID productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        if(!existingProduct.getTenantId().equals(tenantId)){
            throw new InvalidTenantException("Invalid tenant Id.");
        }

        return existingProduct;
    }
}
