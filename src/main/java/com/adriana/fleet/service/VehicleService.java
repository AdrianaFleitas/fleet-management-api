package com.adriana.fleet.service;

import com.adriana.fleet.dto.VehicleRequest;
import com.adriana.fleet.dto.VehicleResponse;
import com.adriana.fleet.entity.Vehicle;
import com.adriana.fleet.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import com.adriana.fleet.exception.DuplicateResourceException;
import com.adriana.fleet.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public VehicleResponse createVehicle(VehicleRequest request) {
        if (vehicleRepository.existsByPlateNumberAndDeletedAtIsNull(request.getPlateNumber())) {
            throw new DuplicateResourceException("Plate number already exists");
        }

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
        return vehicleRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<VehicleResponse> getDeletedVehicles() {
        return vehicleRepository.findAllByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        return mapToResponse(vehicle);
    }

    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        boolean plateNumberChanged = !vehicle.getPlateNumber().equals(request.getPlateNumber());

        if (plateNumberChanged &&
                vehicleRepository.existsByPlateNumberAndDeletedAtIsNull(request.getPlateNumber())) {
            throw new DuplicateResourceException("Plate number already exists");
        }

        vehicle.setPlateNumber(request.getPlateNumber());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setStatus(request.getStatus());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return mapToResponse(updatedVehicle);
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        vehicle.setDeletedAt(LocalDateTime.now());

        vehicleRepository.save(vehicle);
    }

    public VehicleResponse restoreVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (vehicle.getDeletedAt() == null) {
            throw new DuplicateResourceException("Vehicle is not deleted");
        }

        vehicle.setDeletedAt(null);

        Vehicle restoredVehicle = vehicleRepository.save(vehicle);

        return mapToResponse(restoredVehicle);
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getPlateNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getStatus(),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt(),
                vehicle.getDeletedAt()
        );
    }
}