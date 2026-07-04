package com.adriana.fleet.service;

import com.adriana.fleet.dto.VehicleRequest;
import com.adriana.fleet.dto.VehicleResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {

    private final List<VehicleResponse> vehicles = new ArrayList<>();
    private Long nextId = 1L;

    public VehicleResponse createVehicle(VehicleRequest request) {
        VehicleResponse vehicle = new VehicleResponse(
                nextId,
                request.getPlateNumber(),
                request.getBrand(),
                request.getModel(),
                request.getYear(),
                request.getStatus()
        );

        vehicles.add(vehicle);
        nextId++;

        return vehicle;
    }

    public List<VehicleResponse> getAllVehicles() {
        return vehicles;
    }

    public VehicleResponse getVehicleById(Long id) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        for (int i = 0; i < vehicles.size(); i++) {
            VehicleResponse currentVehicle = vehicles.get(i);

            if (currentVehicle.getId().equals(id)) {
                VehicleResponse updatedVehicle = new VehicleResponse(
                        id,
                        request.getPlateNumber(),
                        request.getBrand(),
                        request.getModel(),
                        request.getYear(),
                        request.getStatus()
                );

                vehicles.set(i, updatedVehicle);

                return updatedVehicle;
            }
        }

        return null;
    }
    public boolean deleteVehicle(Long id) {
        return vehicles.removeIf(vehicle -> vehicle.getId().equals(id));
    }
}