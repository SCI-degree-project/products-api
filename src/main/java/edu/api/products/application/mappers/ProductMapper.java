package edu.api.products.application.mappers;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductPreviewDTO;
import edu.api.products.domain.Product;

public class ProductMapper {
    public static Product toEntity(ProductDTO dto) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .materials(dto.materials())
                .style(dto.style())
                .tenantId(dto.tenantId())
                .build();
    }

    public static ProductPreviewDTO toPreview(Product product) {
        return new ProductPreviewDTO(
                product.getId(),
                product.getName()
        );
    }

}
