package com.adriana.fleet.controller;

import com.adriana.fleet.dto.ApiResponse;
import com.adriana.fleet.dto.VehicleRequest;
import com.adriana.fleet.dto.VehicleResponse;
import com.adriana.fleet.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ApiResponse<VehicleResponse> createVehicle(@RequestBody VehicleRequest request) {
        VehicleResponse vehicle = vehicleService.createVehicle(request);

        return new ApiResponse<>(
                true,
                "Vehicle created successfully",
                vehicle
        );
    }

    @GetMapping
    public ApiResponse<List<VehicleResponse>> getAllVehicles() {
        List<VehicleResponse> vehicles = vehicleService.getAllVehicles();

        return new ApiResponse<>(
                true,
                "Vehicles retrieved successfully",
                vehicles
        );
    }
    @GetMapping("/{id}")
    public ApiResponse<VehicleResponse> getVehicleById(@PathVariable Long id) {
        VehicleResponse vehicle = vehicleService.getVehicleById(id);

        if (vehicle == null) {
            return new ApiResponse<>(
                    false,
                    "Vehicle not found",
                    null
            );
        }

        return new ApiResponse<>(
                true,
                "Vehicle retrieved successfully",
                vehicle
        );
    }
    @PutMapping("/{id}")
    public ApiResponse<VehicleResponse> updateVehicle(
            @PathVariable Long id,
            @RequestBody VehicleRequest request
    ) {
        VehicleResponse vehicle = vehicleService.updateVehicle(id, request);

        if (vehicle == null) {
            return new ApiResponse<>(
                    false,
                    "Vehicle not found",
                    null
            );
        }

        return new ApiResponse<>(
                true,
                "Vehicle updated successfully",
                vehicle
        );
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteVehicle(@PathVariable Long id) {
        boolean deleted = vehicleService.deleteVehicle(id);

        if (!deleted) {
            return new ApiResponse<>(
                    false,
                    "Vehicle not found",
                    null
            );
        }

        return new ApiResponse<>(
                true,
                "Vehicle deleted successfully",
                null
        );
    }
}