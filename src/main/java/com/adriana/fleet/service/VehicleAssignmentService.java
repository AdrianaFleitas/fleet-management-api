package com.adriana.fleet.service;

import com.adriana.fleet.dto.VehicleAssignmentRequest;
import com.adriana.fleet.dto.VehicleAssignmentResponse;
import com.adriana.fleet.entity.Driver;
import com.adriana.fleet.entity.Vehicle;
import com.adriana.fleet.entity.VehicleAssignment;
import com.adriana.fleet.exception.DuplicateResourceException;
import com.adriana.fleet.exception.ResourceNotFoundException;
import com.adriana.fleet.repository.DriverRepository;
import com.adriana.fleet.repository.VehicleAssignmentRepository;
import com.adriana.fleet.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleAssignmentService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_RELEASED = "RELEASED";

    private final VehicleAssignmentRepository vehicleAssignmentRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public VehicleAssignmentService(
            VehicleAssignmentRepository vehicleAssignmentRepository,
            VehicleRepository vehicleRepository,
            DriverRepository driverRepository
    ) {
        this.vehicleAssignmentRepository = vehicleAssignmentRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    public VehicleAssignmentResponse createAssignment(VehicleAssignmentRequest request) {
        Vehicle vehicle = vehicleRepository.findByIdAndDeletedAtIsNull(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Driver driver = driverRepository.findByIdAndDeletedAtIsNull(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        if (vehicleAssignmentRepository.existsByVehicleIdAndStatusAndDeletedAtIsNull(
                request.getVehicleId(),
                STATUS_ACTIVE
        )) {
            throw new DuplicateResourceException("Vehicle already has an active assignment");
        }

        if (vehicleAssignmentRepository.existsByDriverIdAndStatusAndDeletedAtIsNull(
                request.getDriverId(),
                STATUS_ACTIVE
        )) {
            throw new DuplicateResourceException("Driver already has an active assignment");
        }

        VehicleAssignment assignment = new VehicleAssignment(
                vehicle,
                driver,
                LocalDateTime.now(),
                STATUS_ACTIVE
        );

        VehicleAssignment savedAssignment = vehicleAssignmentRepository.save(assignment);

        return mapToResponse(savedAssignment);
    }

    public List<VehicleAssignmentResponse> getAllAssignments() {
        return vehicleAssignmentRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<VehicleAssignmentResponse> getDeletedAssignments() {
        return vehicleAssignmentRepository.findAllByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public VehicleAssignmentResponse getAssignmentById(Long id) {
        VehicleAssignment assignment = vehicleAssignmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle assignment not found"));

        return mapToResponse(assignment);
    }

    public VehicleAssignmentResponse releaseAssignment(Long id) {
        VehicleAssignment assignment = vehicleAssignmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle assignment not found"));

        if (!STATUS_ACTIVE.equals(assignment.getStatus())) {
            throw new DuplicateResourceException("Vehicle assignment is not active");
        }

        assignment.setStatus(STATUS_RELEASED);
        assignment.setReleasedAt(LocalDateTime.now());

        VehicleAssignment releasedAssignment = vehicleAssignmentRepository.save(assignment);

        return mapToResponse(releasedAssignment);
    }

    public void deleteAssignment(Long id) {
        VehicleAssignment assignment = vehicleAssignmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle assignment not found"));

        assignment.setDeletedAt(LocalDateTime.now());

        vehicleAssignmentRepository.save(assignment);
    }

    public VehicleAssignmentResponse restoreAssignment(Long id) {
        VehicleAssignment assignment = vehicleAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle assignment not found"));

        if (assignment.getDeletedAt() == null) {
            throw new DuplicateResourceException("Vehicle assignment is not deleted");
        }

        assignment.setDeletedAt(null);

        VehicleAssignment restoredAssignment = vehicleAssignmentRepository.save(assignment);

        return mapToResponse(restoredAssignment);
    }

    private VehicleAssignmentResponse mapToResponse(VehicleAssignment assignment) {
        Vehicle vehicle = assignment.getVehicle();
        Driver driver = assignment.getDriver();

        String driverFullName = driver.getFirstName() + " " + driver.getLastName();

        return new VehicleAssignmentResponse(
                assignment.getId(),
                vehicle.getId(),
                vehicle.getPlateNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                driver.getId(),
                driverFullName,
                driver.getLicenseNumber(),
                assignment.getAssignedAt(),
                assignment.getReleasedAt(),
                assignment.getStatus(),
                assignment.getCreatedAt(),
                assignment.getUpdatedAt(),
                assignment.getDeletedAt()
        );
    }
}