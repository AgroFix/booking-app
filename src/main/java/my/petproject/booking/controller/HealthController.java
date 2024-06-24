package my.petproject.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import my.petproject.booking.dto.health.HealthResponseDto;
import my.petproject.booking.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/health")
public class HealthController {
    private final HealthService healthService;

    @GetMapping
    @Operation(summary = "Get health info", description = "Informs that the application is working")
    public HealthResponseDto healthCheck() {
        return healthService.checkHealth();
    }
}
