package edu.api.products.domain.product;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dimensions {
    private Double width;
    private Double height;
    private Double depth;
}
