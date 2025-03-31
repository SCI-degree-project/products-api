package edu.api.products.application.dto;

import edu.api.products.domain.Material;
import lombok.Data;

import java.util.List;

@Data
public class CreateProductDTO {
    private String productName;
    private String productDescription;
    private double productPrice;
    private List<Material> material;
}
