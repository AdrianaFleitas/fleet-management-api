package com.adriana.fleet.controller;

import com.adriana.fleet.dto.ApiResponse;
import com.adriana.fleet.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/v1/health")
    public ApiResponse<HealthResponse> health() {

        HealthResponse healthResponse = new HealthResponse(
                "UP",
                "Fleet Management Api",
                "1.0.0"
        );

        return new ApiResponse<>(
                true,
                "Health check executed successfully",
                healthResponse
        );
    }
}