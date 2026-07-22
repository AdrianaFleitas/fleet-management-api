package com.adriana.fleet.service;

import com.adriana.fleet.dto.PagedResponse;
import com.adriana.fleet.dto.VehicleAssignmentRequest;
import com.adriana.fleet.dto.VehicleAssignmentResponse;
import com.adriana.fleet.entity.Driver;
import com.adriana.fleet.entity.Vehicle;
import com.adriana.fleet.entity.VehicleAssignment;
import com.adriana.fleet.exception.BadRequestException;
import com.adriana.fleet.exception.DuplicateResourceException;
import com.adriana.fleet.exception.ResourceNotFoundException;
import com.adriana.fleet.repository.DriverRepository;
import com.adriana.fleet.repository.VehicleAssignmentRepository;
import com.adriana.fleet.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.adriana.fleet.constants.AssignmentStatus;
import java.time.LocalDateTime;
import com.adriana.fleet.constants.VehicleAssignmentSortField;
import java.util.List;
import java.util.Set;

@Service
public class VehicleAssignmentService {


    private static final int MAX_PAGE_SIZE = 100;

    private static final Set<String> VALID_STATUSES = Set.of(
            AssignmentStatus.ACTIVE,
            AssignmentStatus.RELEASED
    );

    private static final Set<String> VALID_SORT_FIELDS = Set.of(
            VehicleAssignmentSortField.ID,
            VehicleAssignmentSortField.ASSIGNED_AT,
            VehicleAssignmentSortField.RELEASED_AT,
            VehicleAssignmentSortField.STATUS,
            VehicleAssignmentSortField.CREATED_AT,
            VehicleAssignmentSortField.UPDATED_AT
    );

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
                AssignmentStatus.ACTIVE
        )) {
            throw new DuplicateResourceException("Vehicle already has an active assignment");
        }

        if (vehicleAssignmentRepository.existsByDriverIdAndStatusAndDeletedAtIsNull(
                request.getDriverId(),
                AssignmentStatus.ACTIVE
        )) {
            throw new DuplicateResourceException("Driver already has an active assignment");
        }

        VehicleAssignment assignment = new VehicleAssignment(
                vehicle,
                driver,
                LocalDateTime.now(),
                AssignmentStatus.ACTIVE
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
        validatePaginationAndFilters(status, page, size, sortBy, sortDirection);

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

        if (!AssignmentStatus.ACTIVE.equals(assignment.getStatus())) {
            throw new DuplicateResourceException("Vehicle assignment is not active");
        }

        assignment.setStatus(AssignmentStatus.RELEASED);
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

    private void validatePaginationAndFilters(
            String status,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {
        if (page < 0) {
            throw new BadRequestException("Page must be greater than or equal to 0");
        }

        if (size < 1) {
            throw new BadRequestException("Size must be greater than or equal to 1");
        }

        if (size > MAX_PAGE_SIZE) {
            throw new BadRequestException("Size must be less than or equal to " + MAX_PAGE_SIZE);
        }

        if (status != null && !VALID_STATUSES.contains(status)) {
            throw new BadRequestException("Status must be ACTIVE or RELEASED");
        }

        if (!VALID_SORT_FIELDS.contains(sortBy)) {
            throw new BadRequestException("Invalid sort field");
        }

        if (!"asc".equalsIgnoreCase(sortDirection) && !"desc".equalsIgnoreCase(sortDirection)) {
            throw new BadRequestException("Sort direction must be asc or desc");
        }
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