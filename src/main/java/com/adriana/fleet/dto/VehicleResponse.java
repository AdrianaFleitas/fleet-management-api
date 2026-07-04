package com.adriana.fleet.dto;

public class VehicleResponse {

    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    private int year;
    private String status;

    public VehicleResponse(Long id, String plateNumber, String brand, String model, int year, String status) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.status = status;
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
}