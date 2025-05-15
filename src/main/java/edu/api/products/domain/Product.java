package edu.api.products.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", unique = true, nullable = false)
    UUID id;
    @Column(nullable = false)
    String name;
    String description;
    double price;
    @ElementCollection
    @CollectionTable(name = "product_materials", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    List<Material> materials;
    @Enumerated(EnumType.STRING)
    @Column(name = "style")
    Style style;
    @Column(nullable = false)
    UUID tenantId;
    @ElementCollection
    @CollectionTable(name = "product_gallery", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    List<String> gallery;
    @Column(name = "model_url")
    String model;
}
