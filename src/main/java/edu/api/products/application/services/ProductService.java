package edu.api.products.application.services;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.exceptions.BusinessException;
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
    public void create(ProductDTO product) {
        Product newProduct = ProductMapper.toEntity(product);

        if(newProduct.getName() == null || newProduct.getName().isEmpty()){
            throw new BusinessException("Unable to create the product. Empty name.");
        }

        double MAX_PRICE = 100000;
        if(newProduct.getPrice() <= 0 || newProduct.getPrice() >= MAX_PRICE){
            throw new BusinessException("Unable to create the product. Price out of range.");
        }
        productRepository.save(newProduct);
    }

    @Override
    public void get(UUID id) {
        productRepository.findById(id);
    }

    @Override
    public void update(ProductDTO product) {
        //productRepository.save(product);
    }

    @Override
    public void delete(UUID id) {
        productRepository.deleteById(id);
    }
}
