package edu.api.products.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMetricSummary {
    private UUID productId;
    private String name;
    private int clicks;
    private int arViews;
    private int searchAppearances;
}
