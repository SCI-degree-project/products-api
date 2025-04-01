package edu.api.products.application.dto;

import edu.api.products.domain.Material;

import java.util.List;

public record ProductDTO(
        String productName,
        String productDescription,
        double productPrice,
        List<Material> material) { }
