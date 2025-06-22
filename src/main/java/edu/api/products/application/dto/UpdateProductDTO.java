package edu.api.products.application.dto;

import edu.api.products.domain.Dimensions;
import edu.api.products.domain.Material;
import edu.api.products.domain.Style;

import java.util.List;

public record UpdateProductDTO(
        String name,
        String description,
        Double price,
        List<Material> materials,
        Style style,
        List<String> gallery,
        String model,
        Boolean deleted,
        Boolean visible,
        Dimensions dimensions
) {}
