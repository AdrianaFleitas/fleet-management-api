package com.adriana.fleet.dto;

import jakarta.validation.constraints.NotBlank;

public class DriverRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Status is required")
    private String status;

    public DriverRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}