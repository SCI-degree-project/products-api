package edu.api.products.application.controllers;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductPreviewDTO;
import edu.api.products.application.dto.ProductSearchCriteria;
import edu.api.products.application.dto.UpdateProductDTO;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.InvalidTenantException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.mappers.ProductMapper;
import edu.api.products.application.services.product.ProductService;
import edu.api.products.domain.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            Product product = productService.create(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID productId) {
        try {
            Product product = productService.getById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (InvalidTenantException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{tenantId}/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID tenantId, @PathVariable UUID productId, @RequestBody ProductDTO productDTO) {
        try {
            productService.update(tenantId, productId, productDTO);
            return ResponseEntity.ok().body(productDTO);
        } catch (InvalidTenantException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{tenantId}/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID tenantId, @PathVariable UUID productId) {
        try {
            productService.delete(tenantId, productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (InvalidTenantException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<Page<ProductPreviewDTO>> getProducts(
            @PathVariable UUID tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> products = productService.getProducts(tenantId, pageable);
            return ResponseEntity.ok(products.map(ProductMapper::toPreview));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{tenantId}/all")
    public ResponseEntity<Void> deleteAllProductsByTenant(@PathVariable UUID tenantId) {
        try {
            productService.deleteAllByTenantId(tenantId);
            return ResponseEntity.noContent().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductPreviewDTO>> getProductsBatch(@RequestBody List<UUID> productIds) {
        try {
            List<Product> products = productService.getProductsByIds(productIds);
            List<ProductPreviewDTO> previews = products.stream()
                    .map(ProductMapper::toPreview)
                    .toList();
            return ResponseEntity.ok(previews);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{tenantId}/{productId}")
    public ResponseEntity<ProductDTO> patchProduct(
            @PathVariable UUID tenantId,
            @PathVariable UUID productId,
            @RequestBody UpdateProductDTO dto
    ) {
        try {
            Product updated = productService.partialUpdate(tenantId, productId, dto);
            return ResponseEntity.ok(ProductMapper.toDTO(updated));
        } catch (InvalidTenantException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ProductPreviewDTO>> searchProducts(
            @RequestBody ProductSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            List<String> allowedSortBy = List.of("name", "price");
            List<String> allowedDirections = List.of("asc", "desc");

            String sortBy = criteria.sortBy() != null ? criteria.sortBy() : "name";
            String sortDirection = criteria.direction() != null ? criteria.direction() : "asc";

            if (!allowedSortBy.contains(sortBy.toLowerCase())) {
                return ResponseEntity.badRequest().body(Page.empty());
            }

            if (!allowedDirections.contains(sortDirection.toLowerCase())) {
                return ResponseEntity.badRequest().body(Page.empty());
            }

            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Product> results = productService.search(criteria, pageable);
            Page<ProductPreviewDTO> previews = results.map(ProductMapper::toPreview);

            return ResponseEntity.ok(previews);

        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
