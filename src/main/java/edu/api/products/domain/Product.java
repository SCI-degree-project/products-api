package edu.api.products.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

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
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    private double price;

    @ElementCollection
    @CollectionTable(name = "product_materials", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private List<Material> materials;

    @Enumerated(EnumType.STRING)
    @Column(name = "style")
    private Style style;

    @Column(nullable = false)
    private UUID tenantId;

    @ElementCollection
    @CollectionTable(name = "product_gallery", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> gallery;

    @Column(name = "model_url")
    private String model;

    @Column(nullable = false)
    private boolean isDeleted = Boolean.FALSE;

    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private boolean isVisible = Boolean.TRUE;

    @Embedded
    private Dimensions dimensions;
}
