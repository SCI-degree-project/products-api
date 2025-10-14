package edu.api.products.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryItem {
    private String imageUrl;
    private String altText;
    private String aspectRatio;
}