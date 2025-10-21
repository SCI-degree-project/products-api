package edu.api.products.application.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductMetricSummary(
        UUID productId,
        String productName,
        double rawScore,
        double normalizedScore,
        int clicks,
        int arViews,
        int searchAppearances,
        int favoritesAdds,
        float timeSpentOnProductPageSeconds,
        float timeSpentOnArViewSeconds
) {
}
