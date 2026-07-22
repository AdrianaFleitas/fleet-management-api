package com.adriana.fleet.repository;

import com.adriana.fleet.entity.VehicleAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT assignment
            FROM VehicleAssignment assignment
            WHERE assignment.deletedAt IS NULL
            AND (:status IS NULL OR assignment.status = :status)
            AND (:vehicleId IS NULL OR assignment.vehicle.id = :vehicleId)
            AND (:driverId IS NULL OR assignment.driver.id = :driverId)
            """)
    Page<VehicleAssignment> findFilteredAssignments(
            @Param("status") String status,
            @Param("vehicleId") Long vehicleId,
            @Param("driverId") Long driverId,
            Pageable pageable
    );
}