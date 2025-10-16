package edu.api.products.application.mappers;

import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.dto.ProductPreviewDTO;
import edu.api.products.application.dto.UpdateProductDTO;
import edu.api.products.domain.Product;
import edu.api.products.domain.ProductConstants;
import edu.api.products.domain.ProductStatus;

import java.time.LocalDateTime;

public class ProductMapper {
    public static Product toEntity(ProductDTO dto) {
        ProductStatus status = new ProductStatus();
        if (dto.status() != null) {
            status = dto.status();
        } else {
            status = ProductStatus.builder()
                    .createdAt(LocalDateTime.now())
                    .deleted(false)
                    .deletedAt(null)
                    .visible(true)
                    .build();
        }
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .materials(dto.materials())
                .style(dto.style())
                .tenantId(dto.tenantId())
                .dimensions(dto.dimensions())
                .media(dto.media())
                .status(status)
                .build();
    }

    public static ProductPreviewDTO toPreview(Product product) {
        String cover = null;

        if (product.getMedia().getGallery() != null && !product.getMedia().getGallery().isEmpty()) {
            int index = ProductConstants.PRODUCT_COVER_IMAGE_INDEX;
            cover = product.getMedia().getGallery().get(index).getImageUrl();
        }

        String aspectRatio = null;
        if(product.getMedia().getGallery() != null && !product.getMedia().getGallery().isEmpty()
        && product.getMedia().getGallery().get(0) != null && product.getMedia().getGallery().get(0).getAspectRatio() != null) {
            aspectRatio = product.getMedia().getGallery().get(0).getAspectRatio();
        } else {
            aspectRatio = "1.00";
        }

        return new ProductPreviewDTO(
                product.getId(),
                product.getName() != null ? product.getName() : "No name",
                cover,
                product.getPrice(),
                aspectRatio
        );
    }

    public static void partialUpdate(Product product, UpdateProductDTO dto) {
        if (dto.name() != null) product.setName(dto.name());
        if (dto.description() != null) product.setDescription(dto.description());
        if (dto.price() != null) product.setPrice(dto.price());
        if (dto.materials() != null) product.setMaterials(dto.materials());
        if (dto.style() != null) product.setStyle(dto.style());
        if (dto.media() != null) {
            if (dto.media().getGallery() != null) product.getMedia().setGallery(dto.media().getGallery());
            if (dto.media().getModel() != null) product.getMedia().setModel(dto.media().getModel());
        }
        if(dto.dimensions() != null) {
            if (dto.dimensions().getWidth() != null) product.getDimensions().setWidth(dto.dimensions().getWidth());
            if (dto.dimensions().getHeight() != null) product.getDimensions().setHeight(dto.dimensions().getHeight());
            if (dto.dimensions().getDepth() != null) product.getDimensions().setDepth(dto.dimensions().getDepth());
        }
        if (dto.status() != null) {
            if (dto.status().visible() != null) product.getStatus().setVisible(dto.status().visible());
        }
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
                product.getMedia(),
                product.getStatus(),
                product.getDimensions()
                );
    }
}
