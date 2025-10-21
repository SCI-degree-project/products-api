package edu.api.products.domain.metric;

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

    @Column(name = "raw_score")
    private double rawScore = 0.0;

    @Column(name = "normalized_score")
    private double normalizedScore = 0.0;

    @Column(name = "clicks")
    private int clicks = 0;

    @Column(name = "ar_views")
    private int arViews = 0;

    @Column(name = "search_appearances")
    private int searchAppearances = 0;

    @Column(name = "favorites_adds")
    private int favoritesAdds = 0;

    @Column(name = "time_spent_on_page_seconds")
    private float timeSpentOnProductPageSeconds = 0;

    @Column(name = "time_spent_in_ar_seconds")
    private float timeSpentOnArViewSeconds = 0;
}
