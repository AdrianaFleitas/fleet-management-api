package com.adriana.fleet.dto;

import java.time.LocalDateTime;

public class VehicleResponse {

    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    private int year;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public VehicleResponse(
            Long id,
            String plateNumber,
            String brand,
            String model,
            int year,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Long getId() {
        return id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
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