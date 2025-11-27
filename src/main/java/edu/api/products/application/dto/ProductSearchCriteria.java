package edu.api.products.application.dto;

import edu.api.products.domain.product.Material;
import edu.api.products.domain.product.Style;

import java.util.List;
import java.util.UUID;

public record ProductSearchCriteria(
        String name,
        Style style,
        List<Material> materials,
        String sortBy,
        String direction,
        UUID tenantId
) { }