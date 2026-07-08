package com.adriana.fleet.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class VehicleRequest {

    @NotBlank(message = "Plate number is required")
    private String plateNumber;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1900, message = "Year must be greater than or equal to 1900")
    @Max(value = 2100, message = "Year must be less than or equal to 2100")
    private int year;

    @NotBlank(message = "Status is required")
    private String status;

    public VehicleRequest() {
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
}