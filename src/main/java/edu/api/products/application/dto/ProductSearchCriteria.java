package edu.api.products.application.dto;

import edu.api.products.domain.Material;
import edu.api.products.domain.Style;

import java.util.List;

public record ProductSearchCriteria(
        String name,
        Style style,
        List<Material> materials,
        String sortBy,
        String direction,
        int page,
        int size
) { }