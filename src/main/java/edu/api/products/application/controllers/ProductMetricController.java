package edu.api.products.application.controllers;

import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.services.metrics.ProductMetricService;
import edu.api.products.domain.ProductMetric;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/metrics")
@RequiredArgsConstructor
public class ProductMetricController {

    private final ProductMetricService productMetricService;

    @GetMapping("/metrics/{productId}")
    public ResponseEntity<ProductMetric> getProductMetric(@PathVariable UUID productId) {
        try {
            ProductMetric metric = productMetricService.getProductMetric(productId);
            return ResponseEntity.ok(metric);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/metrics/click/{productId}")
    public ResponseEntity<Void> registerClick(@PathVariable UUID productId) {
        try {
            productMetricService.incrementClickMetric(productId);
            return ResponseEntity.ok().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/metrics/ar-views/{productId}")
    public ResponseEntity<Void> registerArView(@PathVariable UUID productId) {
        try {
            productMetricService.incrementArViewMetric(productId);
            return ResponseEntity.ok().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/metrics/search-appearances/{productId}")
    public ResponseEntity<Void> registerSearchAppearances(@PathVariable UUID productId) {
        try {
            productMetricService.incrementSearchAppearMetric(productId);
            return ResponseEntity.ok().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
