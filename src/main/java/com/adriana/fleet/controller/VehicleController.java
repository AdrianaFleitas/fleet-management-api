package com.adriana.fleet.controller;

import com.adriana.fleet.dto.ApiResponse;
import com.adriana.fleet.dto.VehicleRequest;
import com.adriana.fleet.dto.VehicleResponse;
import com.adriana.fleet.service.VehicleService;
import jakarta.validation.Valid;
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
    public ApiResponse<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest request) {
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

    @GetMapping("/deleted")
    public ApiResponse<List<VehicleResponse>> getDeletedVehicles() {
        List<VehicleResponse> vehicles = vehicleService.getDeletedVehicles();

        return new ApiResponse<>(
                true,
                "Deleted vehicles retrieved successfully",
                vehicles
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<VehicleResponse> getVehicleById(@PathVariable Long id) {
        VehicleResponse vehicle = vehicleService.getVehicleById(id);

        return new ApiResponse<>(
                true,
                "Vehicle retrieved successfully",
                vehicle
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<VehicleResponse> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequest request
    ) {
        VehicleResponse vehicle = vehicleService.updateVehicle(id, request);

        return new ApiResponse<>(
                true,
                "Vehicle updated successfully",
                vehicle
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);

        return new ApiResponse<>(
                true,
                "Vehicle deleted successfully",
                null
        );
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<VehicleResponse> restoreVehicle(@PathVariable Long id) {
        VehicleResponse vehicle = vehicleService.restoreVehicle(id);

        return new ApiResponse<>(
                true,
                "Vehicle restored successfully",
                vehicle
        );
    }
}