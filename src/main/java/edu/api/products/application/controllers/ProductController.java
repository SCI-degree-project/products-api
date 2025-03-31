package edu.api.products.application.controllers;

import edu.api.products.application.dto.CreateProductDTO;
import edu.api.products.application.services.ProductService;
import edu.api.products.domain.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getProductName())
                .description(productDTO.getProductDescription())
                .price(productDTO.getProductPrice())
                .materials(productDTO.getMaterial())
                .build();
        productService.create(product);
        return ResponseEntity.ok(product);
    }
}
