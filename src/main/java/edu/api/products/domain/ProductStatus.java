package edu.api.products.domain;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatus {
    private LocalDateTime createdAt;
    private Boolean deleted = Boolean.FALSE;
    private LocalDateTime deletedAt;
    private Boolean visible = Boolean.TRUE;
}