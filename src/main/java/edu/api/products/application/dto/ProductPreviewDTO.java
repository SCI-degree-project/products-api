package edu.api.products.application.dto;

import java.util.UUID;

public record ProductPreviewDTO(
        UUID id,
        String name
) { }

