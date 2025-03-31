package edu.api.products.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.api.products.application.dto.ProductDTO;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.services.ProductService;
import edu.api.products.domain.Material;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    private ObjectMapper objectMapper = new ObjectMapper();

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
}
