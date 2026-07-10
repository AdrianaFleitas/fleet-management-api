package com.adriana.fleet.controller;

import com.adriana.fleet.dto.ApiResponse;
import com.adriana.fleet.dto.DriverRequest;
import com.adriana.fleet.dto.DriverResponse;
import com.adriana.fleet.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ApiResponse<DriverResponse> createDriver(@Valid @RequestBody DriverRequest request) {
        DriverResponse driver = driverService.createDriver(request);

        return new ApiResponse<>(
                true,
                "Driver created successfully",
                driver
        );
    }

    @GetMapping
    public ApiResponse<List<DriverResponse>> getAllDrivers() {
        List<DriverResponse> drivers = driverService.getAllDrivers();

        return new ApiResponse<>(
                true,
                "Drivers retrieved successfully",
                drivers
        );
    }

    @GetMapping("/deleted")
    public ApiResponse<List<DriverResponse>> getDeletedDrivers() {
        List<DriverResponse> drivers = driverService.getDeletedDrivers();

        return new ApiResponse<>(
                true,
                "Deleted drivers retrieved successfully",
                drivers
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<DriverResponse> getDriverById(@PathVariable Long id) {
        DriverResponse driver = driverService.getDriverById(id);

        return new ApiResponse<>(
                true,
                "Driver retrieved successfully",
                driver
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<DriverResponse> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody DriverRequest request
    ) {
        DriverResponse driver = driverService.updateDriver(id, request);

        return new ApiResponse<>(
                true,
                "Driver updated successfully",
                driver
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);

        return new ApiResponse<>(
                true,
                "Driver deleted successfully",
                null
        );
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<DriverResponse> restoreDriver(@PathVariable Long id) {
        DriverResponse driver = driverService.restoreDriver(id);

        return new ApiResponse<>(
                true,
                "Driver restored successfully",
                driver
        );
    }
}