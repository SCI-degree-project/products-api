package edu.api.products.application.controllers;

import edu.api.products.application.dto.GeneralMetricsReport;
import edu.api.products.application.dto.RegisterMetricDTO;
import edu.api.products.application.dto.RegisterTimeMetricDTO;
import edu.api.products.application.exceptions.BusinessException;
import edu.api.products.application.exceptions.ProductNotFoundException;
import edu.api.products.application.services.metrics.ProductMetricServiceImpl;
import edu.api.products.domain.metric.MetricType;
import edu.api.products.domain.metric.ProductMetric;
import edu.api.products.domain.metric.TimeMetricType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/metrics")
@RequiredArgsConstructor
public class ProductMetricController {

    private final ProductMetricServiceImpl productMetricService;

    @GetMapping("/{productId}")
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

    @PostMapping("/register/{productId}")
    public ResponseEntity<Void> registerMetric(@PathVariable UUID productId, @RequestBody RegisterMetricDTO registerTimeMetricDTO) {
        try {
            MetricType metric = registerTimeMetricDTO.metric();
            switch (metric) {
                case CLICK -> productMetricService.incrementClickMetric(productId);
                case AR_VIEW -> productMetricService.incrementArViewMetric(productId);
                case SEARCH_APPEARANCE -> productMetricService.incrementSearchAppearMetric(productId);
                case FAVORITE_ADD -> productMetricService.incrementFavoritesAdds(productId);
                default -> {
                    return ResponseEntity.badRequest().build();
                }
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/register-time/{productId}")
    public ResponseEntity<Void> registerTimeMetric(@PathVariable UUID productId, @RequestBody RegisterTimeMetricDTO registerTimeMetricDTO) {
        try {
            TimeMetricType metric = registerTimeMetricDTO.metric();
            float duration = registerTimeMetricDTO.duration();

            switch (metric) {
                case TIME_ON_PAGE -> productMetricService.registerTimeOnPage(productId, duration);
                case TIME_ON_AR -> productMetricService.registerTimeInAr(productId, duration);
                default -> {
                    return ResponseEntity.badRequest().build();
                }
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/report/{tenantId}")
    public ResponseEntity<GeneralMetricsReport> getTenantReport(@PathVariable UUID tenantId) {
        try {
            GeneralMetricsReport report = productMetricService.getTenantMetricsReport(tenantId);
            return ResponseEntity.ok(report);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
