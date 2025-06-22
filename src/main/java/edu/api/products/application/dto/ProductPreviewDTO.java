package edu.api.products.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductPreviewDTO(
        UUID id,
        String name,
        String cover,
        double price
) { }
