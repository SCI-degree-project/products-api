package edu.api.products.application.dto;

import edu.api.products.domain.Material;
import edu.api.products.domain.Style;

import java.util.List;
import java.util.UUID;

public record ProductSearchCriteria(
        String name,
        Style style,
        List<Material> materials,
        String sortBy,
        String direction,
        int page,
        int size,
        UUID tenantId
) { }