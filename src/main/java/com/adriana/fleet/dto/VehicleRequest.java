package com.adriana.fleet.dto;

public class VehicleRequest {

    private String plateNumber;
    private String brand;
    private String model;
    private int year;
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