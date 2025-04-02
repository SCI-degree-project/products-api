package edu.api.products.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.services.ProductService;
import edu.api.products.domain.Material;
import edu.api.products.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        ProductDTO productDTO = new ProductDTO("Chair", "A wooden chair", 150.0, List.of(Material.BIRCH_WOOD));

        doNothing().when(productService).create(any(ProductDTO.class));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestForInvalidPrice() throws Exception {
        ProductDTO invalidProductDTO = new ProductDTO("Table", "A wooden table", -5.0, List.of(Material.PINE_WOOD));

        doThrow(new BusinessException("Price out of range"))
                .when(productService).create(any(ProductDTO.class));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProductDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkForValidProduct() throws Exception {
        UUID productId = UUID.fromString("5710f7d0-3aa9-4462-b757-adb50ef039db");
        Product mockProduct = new Product(productId, "Chair", "Wooden chair", 150.0, new ArrayList<>());

        when(productService.get(any(UUID.class))).thenReturn(mockProduct);

        mockMvc.perform(get("/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestForNullProduct() throws Exception {
        doThrow(new BusinessException("Product data must not be null."))
                .when(productService).update(any(UUID.class), any(ProductDTO.class));

        mockMvc.perform(put("/products/e7c08bc7-60e0-46fa-8ff0-1fd444afe0eb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundForInvalidProduct() throws Exception {
        ProductDTO invalidProductDTO = new ProductDTO("Table", "A wooden table", 100.0, List.of(Material.PINE_WOOD));

        doThrow(new ProductNotFoundException("Product not found."))
                .when(productService).update(any(UUID.class), any(ProductDTO.class));

        mockMvc.perform(put("/products/e7c08bc7-60e0-46fa-8ff0-1fd444afe0eb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProductDTO)))
                .andExpect(status().isNotFound());
    }
}
