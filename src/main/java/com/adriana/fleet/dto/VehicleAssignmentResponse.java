package com.adriana.fleet.dto;

import java.time.LocalDateTime;

public class VehicleAssignmentResponse {

    private Long id;

    private Long vehicleId;
    private String vehiclePlateNumber;
    private String vehicleBrand;
    private String vehicleModel;

    private Long driverId;
    private String driverFullName;
    private String driverLicenseNumber;

    private LocalDateTime assignedAt;
    private LocalDateTime releasedAt;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public VehicleAssignmentResponse(
            Long id,
            Long vehicleId,
            String vehiclePlateNumber,
            String vehicleBrand,
            String vehicleModel,
            Long driverId,
            String driverFullName,
            String driverLicenseNumber,
            LocalDateTime assignedAt,
            LocalDateTime releasedAt,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.driverId = driverId;
        this.driverFullName = driverFullName;
        this.driverLicenseNumber = driverLicenseNumber;
        this.assignedAt = assignedAt;
        this.releasedAt = releasedAt;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getDriverFullName() {
        return driverFullName;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public LocalDateTime getReleasedAt() {
        return releasedAt;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}