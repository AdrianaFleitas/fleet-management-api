package com.adriana.fleet.dto;

public class HealthResponse {

    private String status;
    private String application;
    private String version;

    public HealthResponse(String status, String application, String version) {
        this.status = status;
        this.application = application;
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public String getApplication() {
        return application;
    }

    public String getVersion() {
        return version;
    }
}