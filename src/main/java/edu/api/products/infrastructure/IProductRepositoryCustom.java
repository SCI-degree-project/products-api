package edu.api.products.infrastructure;

import edu.api.products.domain.Material;
import edu.api.products.domain.Product;
import edu.api.products.domain.Style;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductRepositoryCustom {
    Page<Product> search(String name, Style style, List<Material> materials, Pageable pageable);
}
