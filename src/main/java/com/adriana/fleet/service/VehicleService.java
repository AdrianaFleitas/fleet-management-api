package com.adriana.fleet.service;

import com.adriana.fleet.dto.VehicleRequest;
import com.adriana.fleet.dto.VehicleResponse;
import com.adriana.fleet.entity.Vehicle;
import com.adriana.fleet.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public VehicleResponse createVehicle(VehicleRequest request) {
        Vehicle vehicle = new Vehicle(
                request.getPlateNumber(),
                request.getBrand(),
                request.getModel(),
                request.getYear(),
                request.getStatus()
        );

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return mapToResponse(savedVehicle);
    }

    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public VehicleResponse getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicle.setPlateNumber(request.getPlateNumber());
                    vehicle.setBrand(request.getBrand());
                    vehicle.setModel(request.getModel());
                    vehicle.setYear(request.getYear());
                    vehicle.setStatus(request.getStatus());

                    Vehicle updatedVehicle = vehicleRepository.save(vehicle);

                    return mapToResponse(updatedVehicle);
                })
                .orElse(null);
    }

    public boolean deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            return false;
        }

        vehicleRepository.deleteById(id);
        return true;
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getPlateNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getStatus()
        );
    }
}