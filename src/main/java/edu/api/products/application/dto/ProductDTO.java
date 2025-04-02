package edu.api.products.application.dto;

import edu.api.products.domain.Material;

import java.util.List;

public record ProductDTO(
        String name,
        String description,
        double price,
        List<Material> materials) { }
