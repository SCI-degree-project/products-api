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
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Product createdProduct = new Product(UUID.randomUUID(), productDTO.name(), productDTO.description(), productDTO.price(), new ArrayList<>());

        when(productService.create(any(ProductDTO.class))).thenReturn(createdProduct);

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
    void shouldCreateAndUpdateAProductSuccessfully() throws Exception {
        ProductDTO productDTO = new ProductDTO("Bed", "A big bed", 1350.0, List.of(Material.BIRCH_WOOD));
        Product createdProduct = new Product(UUID.randomUUID(), productDTO.name(), productDTO.description(), productDTO.price(), new ArrayList<>());


        when(productService.create(any(ProductDTO.class))).thenReturn(createdProduct);

        MvcResult createResult = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        Product createdResponse = objectMapper.readValue(responseBody, Product.class);
        UUID productId = createdResponse.getId();

        ProductDTO productUpdated = new ProductDTO("Big Bed", "A big bed", 1550.0, List.of(Material.BIRCH_WOOD));

        when(productService.get(productId)).thenReturn(createdResponse);

        MvcResult updateResult = mockMvc.perform(put("/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdated)))
                .andExpect(status().isOk())
                .andReturn();

        responseBody = updateResult.getResponse().getContentAsString();
        Product updatedResponse = objectMapper.readValue(responseBody, Product.class);

        String updatedName = updatedResponse.getName();
        Double updatedPrice = updatedResponse.getPrice();

        assertEquals("Big Bed", updatedName);
        assertEquals(1550.0, updatedPrice);
    }

    @Test
    void shouldReturnBadRequestForNullProduct() throws Exception {
        doThrow(new BusinessException("Product data must not be null."))
                .when(productService).update(any(UUID.class), any(ProductDTO.class));

        mockMvc.perform(put("/products/" + null)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundForInvalidProduct() throws Exception {
        UUID productId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        mockMvc.perform(put("/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateAndDeleteProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO("Table", "A wooden Table", 350.0, List.of(Material.BIRCH_WOOD));
        Product createdProduct = new Product(UUID.randomUUID(), productDTO.name(), productDTO.description(), productDTO.price(), new ArrayList<>());


        when(productService.create(any(ProductDTO.class))).thenReturn(createdProduct);

        MvcResult createResult = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        Product createdResponse = objectMapper.readValue(responseBody, Product.class);
        UUID productId = createdResponse.getId();

        when(productService.get(productId)).thenReturn(createdResponse);

        mockMvc.perform(delete("/products/" + productId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundForDeleteProduct() throws Exception {
        UUID productId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        mockMvc.perform(delete("/products/" + productId))
                .andExpect(status().isNotFound());
    }
}
