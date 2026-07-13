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
import com.adriana.fleet.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<VehicleAssignmentResponse> getAssignmentsFiltered(
            String status,
            Long vehicleId,
            Long driverId
    ) {
        List<VehicleAssignment> assignments;

        if (status != null && vehicleId != null) {
            assignments = vehicleAssignmentRepository.findAllByVehicleIdAndStatusAndDeletedAtIsNull(
                    vehicleId,
                    status
            );
        } else if (status != null && driverId != null) {
            assignments = vehicleAssignmentRepository.findAllByDriverIdAndStatusAndDeletedAtIsNull(
                    driverId,
                    status
            );
        } else if (vehicleId != null) {
            assignments = vehicleAssignmentRepository.findAllByVehicleIdAndDeletedAtIsNull(vehicleId);
        } else if (driverId != null) {
            assignments = vehicleAssignmentRepository.findAllByDriverIdAndDeletedAtIsNull(driverId);
        } else if (status != null) {
            assignments = vehicleAssignmentRepository.findAllByStatusAndDeletedAtIsNull(status);
        } else {
            assignments = vehicleAssignmentRepository.findAllByDeletedAtIsNull();
        }

        return assignments.stream()
                .map(this::mapToResponse)
                .toList();
    }
    public PagedResponse<VehicleAssignmentResponse> getAssignmentsFilteredPaged(
            String status,
            Long vehicleId,
            Long driverId,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VehicleAssignment> assignmentPage;

        if (status != null && vehicleId != null) {
            assignmentPage = vehicleAssignmentRepository.findAllByVehicleIdAndStatusAndDeletedAtIsNull(
                    vehicleId,
                    status,
                    pageable
            );
        } else if (status != null && driverId != null) {
            assignmentPage = vehicleAssignmentRepository.findAllByDriverIdAndStatusAndDeletedAtIsNull(
                    driverId,
                    status,
                    pageable
            );
        } else if (vehicleId != null) {
            assignmentPage = vehicleAssignmentRepository.findAllByVehicleIdAndDeletedAtIsNull(
                    vehicleId,
                    pageable
            );
        } else if (driverId != null) {
            assignmentPage = vehicleAssignmentRepository.findAllByDriverIdAndDeletedAtIsNull(
                    driverId,
                    pageable
            );
        } else if (status != null) {
            assignmentPage = vehicleAssignmentRepository.findAllByStatusAndDeletedAtIsNull(
                    status,
                    pageable
            );
        } else {
            assignmentPage = vehicleAssignmentRepository.findAllByDeletedAtIsNull(pageable);
        }

        List<VehicleAssignmentResponse> content = assignmentPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new PagedResponse<>(
                content,
                assignmentPage.getNumber(),
                assignmentPage.getSize(),
                assignmentPage.getTotalElements(),
                assignmentPage.getTotalPages(),
                assignmentPage.isFirst(),
                assignmentPage.isLast()
        );
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