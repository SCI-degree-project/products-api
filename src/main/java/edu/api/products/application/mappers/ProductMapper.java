package edu.api.products.application.mappers;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductPreviewDTO;
import edu.api.products.domain.Product;
import edu.api.products.domain.ProductConstants;

public class ProductMapper {
    public static Product toEntity(ProductDTO dto) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .materials(dto.materials())
                .style(dto.style())
                .tenantId(dto.tenantId())
                .gallery(dto.gallery())
                .model(dto.model())
                .build();
    }

    public static ProductPreviewDTO toPreview(Product product) {
        String cover = null;

        if (product.getGallery() != null && !product.getGallery().isEmpty()) {
            int index = ProductConstants.PRODUCT_COVER_IMAGE_INDEX;
            if (index >= 0 && index < product.getGallery().size()) {
                cover = product.getGallery().get(index);
            }
        }

        return new ProductPreviewDTO(
                product.getId(),
                product.getName() != null ? product.getName() : "Sin nombre",
                cover
        );
    }


}
