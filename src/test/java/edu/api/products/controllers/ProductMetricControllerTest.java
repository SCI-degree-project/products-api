package edu.api.products.controllers;

import edu.api.products.application.controllers.ProductMetricController;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.services.metrics.ProductMetricService;
import edu.api.products.domain.ProductMetric;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductMetricControllerTest {

    private ProductMetricService productMetricService;
    private ProductMetricController controller;

    @BeforeEach
    void setUp() {
        productMetricService = mock(ProductMetricService.class);
        controller = new ProductMetricController(productMetricService);
    }

    @Test
    void testRegisterClick_success() {
        UUID productId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.registerClick(productId);

        verify(productMetricService, times(1)).incrementClickMetric(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegisterClick_productNotFound() {
        UUID productId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-abcdef012345");

        doThrow(new ProductNotFoundException("Product not found"))
                .when(productMetricService).incrementClickMetric(productId);

        ResponseEntity<Void> response = controller.registerClick(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRegisterClick_businessException() {
        UUID productId = UUID.randomUUID();

        doThrow(new BusinessException("Invalid operation"))
                .when(productMetricService).incrementClickMetric(productId);

        ResponseEntity<Void> response = controller.registerClick(productId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRegisterClick_runtimeException() {
        UUID productId = UUID.randomUUID();

        doThrow(new RuntimeException("Something went wrong"))
                .when(productMetricService).incrementClickMetric(productId);

        ResponseEntity<Void> response = controller.registerClick(productId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetProductMetric_success() {
        UUID productId = UUID.randomUUID();
        ProductMetric mockMetric = ProductMetric.builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .clicks(5)
                .build();

        when(productMetricService.getProductMetric(productId)).thenReturn(mockMetric);

        ResponseEntity<ProductMetric> response = controller.getProductMetric(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMetric, response.getBody());
    }

    @Test
    void testGetProductMetric_notFound() {
        UUID productId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-abcdef012345");

        when(productMetricService.getProductMetric(productId))
                .thenThrow(new ProductNotFoundException("Not found"));

        ResponseEntity<ProductMetric> response = controller.getProductMetric(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetProductMetric_businessError() {
        UUID productId = UUID.randomUUID();

        when(productMetricService.getProductMetric(productId))
                .thenThrow(new BusinessException("Business rule violated"));

        ResponseEntity<ProductMetric> response = controller.getProductMetric(productId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetProductMetric_runtimeError() {
        UUID productId = UUID.randomUUID();

        when(productMetricService.getProductMetric(productId))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<ProductMetric> response = controller.getProductMetric(productId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testRegisterArView_success() {
        UUID productId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.registerArView(productId);

        verify(productMetricService, times(1)).incrementArViewMetric(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegisterArView_productNotFound() {
        UUID productId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-abcdef012345");

        doThrow(new ProductNotFoundException("Product not found"))
                .when(productMetricService).incrementArViewMetric(productId);

        ResponseEntity<Void> response = controller.registerArView(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRegisterArView_businessException() {
        UUID productId = UUID.randomUUID();

        doThrow(new BusinessException("Invalid operation"))
                .when(productMetricService).incrementArViewMetric(productId);

        ResponseEntity<Void> response = controller.registerArView(productId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRegisterArView_runtimeException() {
        UUID productId = UUID.randomUUID();

        doThrow(new RuntimeException("Something went wrong"))
                .when(productMetricService).incrementArViewMetric(productId);

        ResponseEntity<Void> response = controller.registerArView(productId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testRegisterSearchAppear_success() {
        UUID productId = UUID.randomUUID();

        ResponseEntity<Void> response = controller.registerSearchAppearances(productId);

        verify(productMetricService, times(1)).incrementSearchAppearMetric(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegisterSearchAppear_productNotFound() {
        UUID productId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-abcdef012345");

        doThrow(new ProductNotFoundException("Product not found"))
                .when(productMetricService).incrementSearchAppearMetric(productId);

        ResponseEntity<Void> response = controller.registerSearchAppearances(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRegisterSearchAppear_businessException() {
        UUID productId = UUID.randomUUID();

        doThrow(new BusinessException("Invalid operation"))
                .when(productMetricService).incrementSearchAppearMetric(productId);

        ResponseEntity<Void> response = controller.registerSearchAppearances(productId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRegisterSearchAppear_runtimeException() {
        UUID productId = UUID.randomUUID();

        doThrow(new RuntimeException("Something went wrong"))
                .when(productMetricService).incrementSearchAppearMetric(productId);

        ResponseEntity<Void> response = controller.registerSearchAppearances(productId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
