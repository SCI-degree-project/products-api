package edu.api.products.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Product {
    UUID id;
    String name;
    String description;
    double price;
    Material material;
}
