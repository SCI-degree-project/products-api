package edu.api.products.application.dto;

import edu.api.products.domain.product.Dimensions;
import edu.api.products.domain.product.Material;
import edu.api.products.domain.product.Media;
import edu.api.products.domain.product.Style;

import java.util.List;

public record UpdateProductDTO(
        String name,
        String description,
        Double price,
        List<Material> materials,
        Style style,
        Media media,
        UpdateProductStatusDTO status,
        Dimensions dimensions
) {}
