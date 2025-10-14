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

    @Embedded
    private Media media;

    @Embedded
    private ProductStatus status;

    @Embedded
    private Dimensions dimensions;
}
