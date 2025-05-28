package edu.api.products.application.services;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductSearchCriteria;
import edu.api.products.application.dto.UpdateProductDTO;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.InvalidTenantException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.mappers.ProductMapper;
import edu.api.products.domain.Product;
import edu.api.products.domain.ProductConstants;
import edu.api.products.infrastructure.IProductRepository;
import edu.api.products.infrastructure.IProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final IProductRepositoryCustom productRepositoryCustom;

    public ProductService(IProductRepository productRepository, IProductRepositoryCustom productRepositoryCustom) {
        this.productRepository = productRepository;
        this.productRepositoryCustom = productRepositoryCustom;
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
    public Product getById(UUID productId) {
        if (productId == null) {
            throw new BusinessException("Product ID must not be null.");
        }

        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
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

        Product existingProduct = validateProductExistence(tenantId, productId);

        existingProduct.setName(product.name());
        existingProduct.setDescription(product.description());
        existingProduct.setPrice(product.price());
        existingProduct.setMaterials(product.materials());
        existingProduct.setStyle(product.style());
        existingProduct.setGallery(product.gallery());
        existingProduct.setModel(product.model());

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

        validateProductExistence(tenantId, id);

        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> getProducts(UUID tenantId, Pageable pageable) {
        if (tenantId == null) {
            throw new BusinessException("Tenant Id must not be null.");
        }
        return productRepository.findAllByTenantId(tenantId, pageable);
    }

    @Override
    public void deleteAllByTenantId(UUID tenantId) {
        if (tenantId == null) {
            throw new BusinessException("Tenant Id must not be null.");
        }
        productRepository.deleteByTenantId(tenantId);
    }


    private void validateProduct(Product product) {
        if(product.getName() == null || product.getName().isEmpty()){
            throw new BusinessException("Unable to create the product. Empty name.");
        }

        if(product.getPrice() <= 0 || product.getPrice() >= ProductConstants.MAX_PRODUCT_PRICE){
            throw new BusinessException("Unable to create the product. Price out of range.");
        }
    }

    private Product validateProductExistence(UUID tenantId, UUID productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        if(!existingProduct.getTenantId().equals(tenantId)){
            throw new InvalidTenantException("Invalid tenant Id.");
        }

        return existingProduct;
    }

    @Override
    public List<Product> getProductsByIds(List<UUID> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new BusinessException("Product ID list must not be null or empty.");
        }

        return productRepository.findAllByIdIn(productIds);
    }

    @Override
    public Product partialUpdate(UUID tenantId, UUID productId, UpdateProductDTO dto) {
        if (tenantId == null || productId == null) {
            throw new BusinessException("IDs must not be null.");
        }

        Product existingProduct = validateProductExistence(tenantId, productId);

        ProductMapper.partialUpdate(existingProduct, dto);

        validateProduct(existingProduct);

        return productRepository.save(existingProduct);
    }

    @Override
    public Page<Product> searchProducts(ProductSearchCriteria criteria) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(criteria.direction()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                Optional.ofNullable(criteria.sortBy()).orElse("name")
        );

        Pageable pageable = PageRequest.of(criteria.page(), criteria.size(), sort);

        return productRepositoryCustom.search(
                criteria.name(),
                criteria.style(),
                criteria.materials(),
                pageable
        );
    }
}
