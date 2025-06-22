package edu.api.products.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product_metrics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "metric_id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "clicks")
    private int clicks;

    @Column(name = "ar_views")
    private int arViews;

    @Column(name = "search_appearances")
    private int searchAppearances;
}
