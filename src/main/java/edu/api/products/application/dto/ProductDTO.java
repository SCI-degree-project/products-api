package edu.api.products.application.dto;

import edu.api.products.domain.Material;
import edu.api.products.domain.Style;

import java.util.List;
import java.util.UUID;

public record ProductDTO(
        String name,
        String description,
        double price,
        List<Material> materials,
        Style style,
        UUID tenantId) { }
