package com.adriana.fleet.repository;

import com.adriana.fleet.entity.VehicleAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleAssignmentRepository extends JpaRepository<VehicleAssignment, Long> {

    List<VehicleAssignment> findAllByDeletedAtIsNull();

    List<VehicleAssignment> findAllByDeletedAtIsNotNull();

    Optional<VehicleAssignment> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByVehicleIdAndStatusAndDeletedAtIsNull(Long vehicleId, String status);

    boolean existsByDriverIdAndStatusAndDeletedAtIsNull(Long driverId, String status);

    List<VehicleAssignment> findAllByStatusAndDeletedAtIsNull(String status);

    List<VehicleAssignment> findAllByVehicleIdAndDeletedAtIsNull(Long vehicleId);

    List<VehicleAssignment> findAllByDriverIdAndDeletedAtIsNull(Long driverId);

    List<VehicleAssignment> findAllByVehicleIdAndStatusAndDeletedAtIsNull(Long vehicleId, String status);

    List<VehicleAssignment> findAllByDriverIdAndStatusAndDeletedAtIsNull(Long driverId, String status);

    Page<VehicleAssignment> findAllByDeletedAtIsNull(Pageable pageable);

    Page<VehicleAssignment> findAllByStatusAndDeletedAtIsNull(String status, Pageable pageable);

    Page<VehicleAssignment> findAllByVehicleIdAndDeletedAtIsNull(Long vehicleId, Pageable pageable);

    Page<VehicleAssignment> findAllByDriverIdAndDeletedAtIsNull(Long driverId, Pageable pageable);

    Page<VehicleAssignment> findAllByVehicleIdAndStatusAndDeletedAtIsNull(
            Long vehicleId,
            String status,
            Pageable pageable
    );

    Page<VehicleAssignment> findAllByDriverIdAndStatusAndDeletedAtIsNull(
            Long driverId,
            String status,
            Pageable pageable
    );
}