package com.adriana.fleet.dto;

import jakarta.validation.constraints.NotNull;

public class VehicleAssignmentRequest {

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Driver ID is required")
    private Long driverId;

    public VehicleAssignmentRequest() {
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public Long getDriverId() {
        return driverId;
    }
}