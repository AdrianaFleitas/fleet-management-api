package com.adriana.fleet.controller;

import com.adriana.fleet.dto.ApiResponse;
import com.adriana.fleet.dto.VehicleAssignmentRequest;
import com.adriana.fleet.dto.VehicleAssignmentResponse;
import com.adriana.fleet.service.VehicleAssignmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.adriana.fleet.dto.PagedResponse;
import java.util.List;
import com.adriana.fleet.constants.PaginationDefaults;
import com.adriana.fleet.constants.VehicleAssignmentSortField;
@RestController
@RequestMapping("/api/v1/vehicle-assignments")
public class VehicleAssignmentController {

    private final VehicleAssignmentService vehicleAssignmentService;

    public VehicleAssignmentController(VehicleAssignmentService vehicleAssignmentService) {
        this.vehicleAssignmentService = vehicleAssignmentService;
    }

    @PostMapping
    public ApiResponse<VehicleAssignmentResponse> createAssignment(
            @Valid @RequestBody VehicleAssignmentRequest request
    ) {
        VehicleAssignmentResponse assignment = vehicleAssignmentService.createAssignment(request);

        return new ApiResponse<>(
                true,
                "Vehicle assignment created successfully",
                assignment
        );
    }

    @GetMapping
    public ApiResponse<PagedResponse<VehicleAssignmentResponse>> getAllAssignments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Long driverId,
            @RequestParam(defaultValue = PaginationDefaults.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PaginationDefaults.DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = VehicleAssignmentSortField.ASSIGNED_AT) String sortBy,
            @RequestParam(defaultValue = PaginationDefaults.DEFAULT_SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<VehicleAssignmentResponse> assignments = vehicleAssignmentService.getAssignmentsFilteredPaged(
                status,
                vehicleId,
                driverId,
                page,
                size,
                sortBy,
                sortDirection
        );

        return new ApiResponse<>(
                true,
                "Vehicle assignments retrieved successfully",
                assignments
        );
    }

    @GetMapping("/deleted")
    public ApiResponse<List<VehicleAssignmentResponse>> getDeletedAssignments() {
        List<VehicleAssignmentResponse> assignments = vehicleAssignmentService.getDeletedAssignments();

        return new ApiResponse<>(
                true,
                "Deleted vehicle assignments retrieved successfully",
                assignments
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<VehicleAssignmentResponse> getAssignmentById(@PathVariable Long id) {
        VehicleAssignmentResponse assignment = vehicleAssignmentService.getAssignmentById(id);

        return new ApiResponse<>(
                true,
                "Vehicle assignment retrieved successfully",
                assignment
        );
    }

    @PatchMapping("/{id}/release")
    public ApiResponse<VehicleAssignmentResponse> releaseAssignment(@PathVariable Long id) {
        VehicleAssignmentResponse assignment = vehicleAssignmentService.releaseAssignment(id);

        return new ApiResponse<>(
                true,
                "Vehicle assignment released successfully",
                assignment
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAssignment(@PathVariable Long id) {
        vehicleAssignmentService.deleteAssignment(id);

        return new ApiResponse<>(
                true,
                "Vehicle assignment deleted successfully",
                null
        );
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<VehicleAssignmentResponse> restoreAssignment(@PathVariable Long id) {
        VehicleAssignmentResponse assignment = vehicleAssignmentService.restoreAssignment(id);

        return new ApiResponse<>(
                true,
                "Vehicle assignment restored successfully",
                assignment
        );
    }
}