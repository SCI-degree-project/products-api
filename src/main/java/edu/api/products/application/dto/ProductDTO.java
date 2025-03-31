package edu.api.products.application.dto;

import edu.api.products.domain.Material;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
public class ProductDTO {
    String productName;
    String productDescription;
    double productPrice;
    List<Material> material;
}
