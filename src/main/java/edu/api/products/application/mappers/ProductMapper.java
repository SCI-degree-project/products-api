package edu.api.products.application.mappers;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductPreviewDTO;
import edu.api.products.application.dto.UpdateProductDTO;
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
                cover,
                product.getPrice()
        );
    }

    public static void partialUpdate(Product product, UpdateProductDTO dto) {
        if (dto.name() != null) product.setName(dto.name());
        if (dto.description() != null) product.setDescription(dto.description());
        if (dto.price() != null) product.setPrice(dto.price());
        if (dto.materials() != null) product.setMaterials(dto.materials());
        if (dto.style() != null) product.setStyle(dto.style());
        if (dto.gallery() != null) product.setGallery(dto.gallery());
        if (dto.model() != null) product.setModel(dto.model());
        if (dto.dimensions() != null) product.setDimensions(dto.dimensions());
        if (dto.isVisible() != null) product.setVisible(dto.isVisible());
        if (dto.isDeleted() != null) product.setDeleted(dto.isDeleted());
    }

    public static ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getMaterials(),
                product.getStyle(),
                product.getTenantId(),
                product.getGallery(),
                product.getModel(),
                product.getDimensions(),
                product.isVisible(),
                product.isDeleted()
        );
    }
}
