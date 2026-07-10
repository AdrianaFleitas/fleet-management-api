package com.adriana.fleet.repository;

import com.adriana.fleet.entity.VehicleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleAssignmentRepository extends JpaRepository<VehicleAssignment, Long> {

    List<VehicleAssignment> findAllByDeletedAtIsNull();

    List<VehicleAssignment> findAllByDeletedAtIsNotNull();

    Optional<VehicleAssignment> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByVehicleIdAndStatusAndDeletedAtIsNull(Long vehicleId, String status);

    boolean existsByDriverIdAndStatusAndDeletedAtIsNull(Long driverId, String status);
}