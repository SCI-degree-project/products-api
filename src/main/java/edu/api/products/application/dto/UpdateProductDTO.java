package edu.api.products.application.dto;

import edu.api.products.domain.*;

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
