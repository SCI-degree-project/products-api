package edu.api.products.application.mappers;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.domain.Product;

public class ProductMapper {
    public static Product toEntity(ProductDTO dto) {
        return Product.builder()
                .name(dto.productName())
                .description(dto.productDescription())
                .price(dto.productPrice())
                .materials(dto.material())
                .build();
    }
}
