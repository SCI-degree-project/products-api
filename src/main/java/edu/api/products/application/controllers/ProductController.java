package edu.api.products.application.controllers;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductPreviewDTO;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.InvalidTenantException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.mappers.ProductMapper;
import edu.api.products.application.services.ProductService;
import edu.api.products.domain.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{tenantId}/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID tenantId, @PathVariable UUID productId) {
        try {
            Product product = productService.get(tenantId, productId);
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
            @RequestParam(defaultValue = "10") int size
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
}
