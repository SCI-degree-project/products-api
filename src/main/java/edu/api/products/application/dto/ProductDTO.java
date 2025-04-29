package edu.api.products.application.dto;

import edu.api.products.domain.Material;
import edu.api.products.domain.Style;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

public record ProductDTO(
        @NotBlank String name,
        String description,
        @Positive double price,
        @NotNull List<Material> materials,
        @NotNull Style style,
        @NotNull UUID tenantId
) { }
