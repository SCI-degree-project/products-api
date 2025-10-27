package edu.api.products.application.services.product;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductPreviewDTO;
import edu.api.products.application.dto.ProductSearchCriteria;
import edu.api.products.application.dto.UpdateProductDTO;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.InvalidTenantException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.mappers.ProductMapper;
import edu.api.products.application.services.image.ImageService;
import edu.api.products.domain.product.Material;
import edu.api.products.domain.product.Product;
import edu.api.products.domain.ProductConstants;
import edu.api.products.domain.metric.ProductMetric;
import edu.api.products.domain.product.Style;
import edu.api.products.infrastructure.product.ProductRepository;
import edu.api.products.infrastructure.product.ProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final ImageService imageService;

    public ProductServiceImpl(ProductRepository productRepository, ProductRepositoryCustom productRepositoryCustom, ImageService imageService) {
        this.productRepository = productRepository;
        this.productRepositoryCustom = productRepositoryCustom;
        this.imageService = imageService;
    }

    @Override
    public Product create(ProductDTO product) {
        if (product == null) {
            throw new BusinessException("Product data must not be null.");
        }

        Product newProduct = ProductMapper.toEntity(product);

        validateProduct(newProduct);

        ProductMetric.builder()
                .productId(newProduct.getId())
                .build();

        calculateAspectRatio(newProduct);

        return productRepository.save(newProduct);
    }

    @Override
    public Product getById(UUID productId) {
        if (productId == null) {
            throw new BusinessException("Product ID must not be null.");
        }

        return productRepository.findByIdAndStatus_DeletedFalse(productId)
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
        existingProduct.getMedia().setGallery(product.media().getGallery());
        existingProduct.getMedia().setModel(product.media().getModel());
        existingProduct.getStatus().setVisible(product.status().getVisible());

        validateProduct(existingProduct);

        calculateAspectRatio(existingProduct);

        return productRepository.save(existingProduct);
    }

    @Override
    public void delete(UUID tenantId, UUID id) {
        if (tenantId == null || id == null) {
            throw new BusinessException("IDs must not be null.");
        }

        Product product = validateProductExistence(tenantId, id);
        product.getStatus().setDeleted(true);
        product.getStatus().setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public Page<Product> getProducts(UUID tenantId, Pageable pageable) {
        if (tenantId == null) {
            throw new BusinessException("Tenant Id must not be null.");
        }
        return productRepository.findAllByTenantIdAndStatus_DeletedFalse(tenantId, pageable);
    }

    @Override
    public void deleteAllByTenantId(UUID tenantId) {
        if (tenantId == null) {
            throw new BusinessException("Tenant Id must not be null.");
        }
        productRepository.deleteByTenantId(tenantId);
    }

    @Override
    public List<Product> getBatchProductsByIds(List<UUID> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new BusinessException("Product ID list must not be null or empty.");
        }

        return productRepository.findAllByIdInAndStatus_DeletedFalse(productIds);
    }

    @Override
    public Product partialUpdate(UUID tenantId, UUID productId, UpdateProductDTO dto) {
        if (tenantId == null || productId == null) {
            throw new BusinessException("IDs must not be null.");
        }

        Product existingProduct = validateProductExistence(tenantId, productId);

        ProductMapper.partialUpdate(existingProduct, dto);

        validateProduct(existingProduct);

        if (existingProduct.getMedia() != null && existingProduct.getMedia().getGallery() != null
                && !existingProduct.getMedia().getGallery().isEmpty() && existingProduct.getMedia().getGallery().get(0) != null) {
            calculateAspectRatio(existingProduct);
        }

        return productRepository.save(existingProduct);
    }

    @Override
    public Page<Product> search(ProductSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            throw new BusinessException("Search criteria must not be null.");
        }
        return productRepositoryCustom.search(criteria, pageable);
    }

    @Override
    public Page<Product> getProductsByMaterial(UUID tenantId, Material material, Pageable pageable) {
        if (tenantId == null) {
            throw new BusinessException("Tenant Id must not be null.");
        }
        return productRepository.findAllByTenantIdAndMaterial(tenantId, material, pageable);
    }

    @Override
    public Page<Product> getProductsByStyle(UUID tenantId, Style style, Pageable pageable) {
        if (tenantId == null) {
            throw new BusinessException("Tenant Id must not be null.");
        }
        return productRepository.findAllByTenantIdAndStyle(tenantId, style, pageable);
    }

    @Override
    public Page<Product> getScoredProducts(Pageable pageable) {
        Page<Product> page = productRepository.findAllByScore(pageable);
        List<Product> products = new ArrayList<>(page.getContent());
        Collections.shuffle(products);
        return new PageImpl<>(products, pageable, page.getTotalElements());
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

    private void calculateAspectRatio(Product product) {
        System.out.println(product.getMedia().getGallery().get(0).getImageUrl());
        if (!product.getMedia().getGallery().get(0).getImageUrl().isEmpty() ||
                product.getMedia().getGallery().get(0).getImageUrl() != null) {
            try {
                String aspectRatio = imageService.calculateAspectRatio(product.getMedia().getGallery().get(0).getImageUrl());
                product.getMedia().getGallery().get(0).setAspectRatio(aspectRatio);
            } catch (RuntimeException e) {
                product.getMedia().getGallery().get(0).setAspectRatio("1.00");
                throw new RuntimeException("Could not calculate aspect ratio", e);
            }
        }
    }
}
