package edu.api.products.domain.product;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    @ElementCollection
    @CollectionTable(name = "product_gallery", joinColumns = @JoinColumn(name = "product_id"))
    private List<GalleryItem> gallery;

    @Column(name = "model3d_url")
    private String model;
}